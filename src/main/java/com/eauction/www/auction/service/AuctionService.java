package com.eauction.www.auction.service;

import com.eauction.www.auction.constants.ErrorConstants;
import com.eauction.www.auction.dto.AuctionEntity;
import com.eauction.www.auction.dto.ItemEntity;
import com.eauction.www.auction.exception.AuctionServiceException;
import com.eauction.www.auction.models.Auction;
import com.eauction.www.auction.models.AuctionStatus;
import com.eauction.www.auction.models.Item;
import com.eauction.www.auction.models.ServiceErrorCode;
import com.eauction.www.auction.search.models.AuctionSearchCriteria;
import com.eauction.www.auction.repository.AuctionRepository;
import com.eauction.www.auction.specification.AuctionSpecifications;
import com.eauction.www.auction.util.ConverterUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public List<Auction> getAuctions() {
        return ConverterUtility.convertToAuctionList(auctionRepository.findAll());
    }

    public Page<Auction> getAuctions(AuctionSearchCriteria criteria) {
        // Calculate pagination safely
        int limit = criteria.getLimit();
        int offset = criteria.getOffset();
        int pageNumber = (limit > 0) ? (offset / limit) : 0;

        Pageable pageable = PageRequest.of(pageNumber, limit, Sort.by("createdTimestamp").descending());

        // Combine filters from the DTO
        Specification<AuctionEntity> spec = Specification.allOf(
                AuctionSpecifications.hasStatus(criteria.getStatus()),
                AuctionSpecifications.nameContains(criteria.getName()),
                AuctionSpecifications.startsAfter(criteria.getFrom()),
                AuctionSpecifications.startsBefore(criteria.getTo()),
                AuctionSpecifications.usernameEquals(criteria.getUsername())
        );

        return auctionRepository.findAll(spec, pageable).map(Auction::new);
    }

    public List<Auction> getAuctions(String username) {
     return ConverterUtility.convertToAuctionList(auctionRepository.findByUsername(username));
    }

    public List<Auction> getAuctions(AuctionStatus status) {
        return ConverterUtility.convertToAuctionList(auctionRepository.findByStatus(status));
    }

    public List<Auction> getAuctions(long fromTimestamp, long toTimestamp) {
        return ConverterUtility.convertToAuctionList(
                auctionRepository.findAuctionsBetweenStartTimestamp(fromTimestamp,toTimestamp));

    }

    public Auction createAuction(Auction auction, Authentication authentication) {

        String username = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        log.info("Creating auction with Name: {} by user: {}", auction.getAuctionName(), username);

        validateUser(auction, isAdmin);

        if (!validateTime(auction)) {
            throw new AuctionServiceException(
                    ErrorConstants.WRONG_AUCTION_TIMESTAMP,
                    ServiceErrorCode.WRONG_AUCTION_TIMESTAMP
            );
        }

        auction.setStatus(AuctionStatus.AWAITING_APPROVAL);
        auction.setCreatedBy(username);

        /**
         * If created by normal user → assign ownership
         * If created by admin → allow flexible ownership (can be null or passed explicitly later)
         */
        if (!isAdmin) {
            auction.setUsername(username);
        }

        log.info("Saving auction with Name: {} by user: {}", auction.getAuctionName(), username);

        return new Auction(
                auctionRepository.save(new AuctionEntity(auction))
        );
    }

    private boolean validateTime(Auction auction) {
        return auction.getStartTimestamp() > Instant.now().toEpochMilli()
                && auction.getStopTimestamp() > Instant.now().toEpochMilli()
                && auction.getStopTimestamp() > auction.getStartTimestamp();
    }

    private boolean validateUser(Auction auction, boolean isCreatedByAdmin) {
        return true;
        // TODO: validate the username if createdBy Admin
    }

    public Auction getAuctionViaAuctionId(String auctionId) {
        return auctionRepository.findByAuctionId(auctionId)
                .map(Auction::new).orElse(null);
    }

    public Auction getAuctionViaIdAndUsername(String auctionId,String username) {
       return auctionRepository.findByAuctionIdAndUsername(auctionId,username)
               .map(Auction::new).orElse(null);
    }

    public boolean isAuctionActive(String auctionId) {
        return AuctionStatus.IN_PROGRESS.equals(getAuctionViaAuctionId(auctionId).getStatus());
    }

    public boolean isAuctionFinished(String auctionId) {
        return AuctionStatus.FINISHED.equals(getAuctionViaAuctionId(auctionId).getStatus());
    }
    public boolean isResultDeclared(String auctionId) {
        Auction auction = getAuctionViaAuctionId(auctionId);
        return AuctionStatus.FINISHED.equals(auction.getStatus()) && auction.isResultDeclared();
    }


    public List<Item> getItemsForAuction(String auctionId) {
        Auction auction = getAuctionViaAuctionId(auctionId);
        if (null != auction) {
            return auction.getItems();
        } else {
            throw new AuctionServiceException("Invalid AuctionId",ServiceErrorCode.INVALID_AUCTION_ID);
        }
    }

    public Auction getAuctionViaId(String auctionId) {
        return auctionRepository.findByAuctionId(auctionId)
                .map(Auction::new).orElse(null);
    }

    public List<Auction> getAuctionsViaAuctionStatus(AuctionStatus auctionStatus) {
        return ConverterUtility.convertToAuctionList(auctionRepository.findByStatus(auctionStatus));
    }

    @Transactional
    public boolean deleteAuction(String auctionId, Authentication authentication) {
        String username = authentication.getName();

        // 1. Fetch the Entity directly using both ID and Username for security
        AuctionEntity auctionEntity = auctionRepository.findByAuctionIdAndUsername(auctionId, username)
                .orElseThrow(() -> new AuctionServiceException(
                        "Auction not found or you are not the owner",
                        ServiceErrorCode.INVALID_AUCTION_ID));

        // 2. State Validation: Allow UPCOMING or REJECTED
        AuctionStatus currentStatus = auctionEntity.getStatus();
        boolean isDeletable = currentStatus == AuctionStatus.UPCOMING
                || currentStatus == AuctionStatus.REJECTED;

        if (!isDeletable) {
            throw new AuctionServiceException(
                    "Only upcoming or rejected auctions can be deleted. Current status: " + currentStatus,
                    ServiceErrorCode.AUCTION_NOT_UPCOMING);
        }

        // 3. Delete from Repository
        auctionRepository.delete(auctionEntity);

        log.info("User {} deleted auction {}", username, auctionId);
        return true;
    }

    public Auction approveAuction(String auctionId) {
        Auction auction = getAuctionViaId(auctionId);
        if (null != auction) {
            if (AuctionStatus.AWAITING_APPROVAL != auction.getStatus()) {
                throw new AuctionServiceException("Only auctions awaiting approval can be approved", ServiceErrorCode.AUCTION_NOT_AWAITING_APPROVAL);

            }
            auction.setStatus(AuctionStatus.UPCOMING);
            return new Auction(auctionRepository.save(new AuctionEntity(auction)));
        } else {
            throw new AuctionServiceException("Invalid AuctionId", ServiceErrorCode.INVALID_AUCTION_ID);
        }
    }

    public Auction rejectAuction(String auctionId, String reason) {
        Auction auction = getAuctionViaId(auctionId);
        if (null != auction) {
            if (AuctionStatus.AWAITING_APPROVAL != auction.getStatus()) {
                throw new AuctionServiceException("Only auctions awaiting approval can be rejected", ServiceErrorCode.AUCTION_NOT_AWAITING_APPROVAL);

            }
            auction.setStatus(AuctionStatus.REJECTED);
            auction.setReasonForCancellation(reason);
            return new Auction(auctionRepository.save(new AuctionEntity(auction)));
        } else {
            throw new AuctionServiceException("Invalid AuctionId", ServiceErrorCode.INVALID_AUCTION_ID);
        }
    }

    @Transactional
    public void updateAuction(String auctionId, Auction updatedAuction, Authentication authentication) {
        // 1. Fetch Entity directly using both ID and Username for security
        AuctionEntity existingEntity = auctionRepository.findByAuctionIdAndUsername(auctionId, authentication.getName())
                .orElseThrow(() -> new AuctionServiceException(
                        "Auction not found or you do not have permission to edit it.",
                        ServiceErrorCode.INVALID_AUCTION_ID));

        // 2. State Validation (Whitelist Approach)
        boolean isEditable = existingEntity.getStatus() == AuctionStatus.AWAITING_APPROVAL
                || existingEntity.getStatus() == AuctionStatus.UPCOMING;

        if (!isEditable) {
            throw new AuctionServiceException(
                    "Only auctions in AWAITING_APPROVAL or UPCOMING status can be updated. Current status: " + existingEntity.getStatus(),
                    ServiceErrorCode.AUCTION_NOT_UPCOMING
            );
        }

        // 3. Update fields (Selective Mapping)
        existingEntity.setAuctionName(updatedAuction.getAuctionName());
        existingEntity.setAuctionDescription(updatedAuction.getAuctionDescription());
        existingEntity.setStartTimestamp(updatedAuction.getStartTimestamp());
        existingEntity.setStopTimestamp(updatedAuction.getStopTimestamp());

        // 4. Reset status for re-approval
        // This ensures that even an "UPCOMING" (already approved) auction
        // goes back to the admin if the user changes the name or dates.
        existingEntity.setStatus(AuctionStatus.AWAITING_APPROVAL);

        // 5. Save (JPA identifies the change and performs the update)
        auctionRepository.save(existingEntity);
    }

    public int getLiveAuctions() {
        return auctionRepository.countByStatus(AuctionStatus.IN_PROGRESS);
    }

    public int getTotalAwaitingApprovalAuctions() {
        return auctionRepository.countByStatus(AuctionStatus.AWAITING_APPROVAL);
    }

    public double getTotalVolume() {
        List<AuctionEntity> finishedAuctions = auctionRepository.findByStatus(AuctionStatus.FINISHED);
        return finishedAuctions.stream()
                .flatMap(auction -> auction.getItems().stream())
                .mapToDouble(ItemEntity::getItemStartPrice)
                .sum();
    }

    public Auction publishAuction(String auctionId) {
        Auction auction = getAuctionViaId(auctionId);
        if (null != auction) {
            if (AuctionStatus.FINISHED != auction.getStatus()) {
                throw new AuctionServiceException("Only finished auctions can be published", ServiceErrorCode.AUCTION_NOT_FINISHED);

            }
            auction.setResultDeclared(true);
            return new Auction(auctionRepository.save(new AuctionEntity(auction)));
        } else {
            throw new AuctionServiceException("Invalid AuctionId", ServiceErrorCode.INVALID_AUCTION_ID);
        }
    }

    public Auction temporarilyStopAuction(String auctionId) {
        Auction auction = getAuctionViaId(auctionId);
        if (null != auction) {
            if (AuctionStatus.IN_PROGRESS != auction.getStatus()) {
                throw new AuctionServiceException("Only active auctions can be temporarily stopped", ServiceErrorCode.AUCTION_NOT_ACTIVE);

            }
            auction.setTemporarilyStopped(true);
            return new Auction(auctionRepository.save(new AuctionEntity(auction)));
        } else {
            throw new AuctionServiceException("Invalid AuctionId", ServiceErrorCode.INVALID_AUCTION_ID);
        }
    }

    public Auction resumeAuction(String auctionId) {
        Auction auction = getAuctionViaId(auctionId);
        if (null != auction) {
            if (AuctionStatus.IN_PROGRESS != auction.getStatus()) {
                throw new AuctionServiceException("Only active auctions can be resumed", ServiceErrorCode.AUCTION_NOT_ACTIVE);

            }
            auction.setTemporarilyStopped(false);
            return new Auction(auctionRepository.save(new AuctionEntity(auction)));
        } else {
            throw new AuctionServiceException("Invalid AuctionId", ServiceErrorCode.INVALID_AUCTION_ID);
        }
    }
}
