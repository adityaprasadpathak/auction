package com.eauction.www.auction.util;

import com.eauction.www.auction.dto.UserEntity;
import com.eauction.www.auction.models.Auction;
import com.eauction.www.auction.models.Item;
import com.eauction.www.auction.models.UserRegistration;
import org.apache.commons.math3.util.Precision;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Utility {

    public static List<Auction> getAuctions() {

        return createSampleAuctions();

    }

    public static List<Auction> getAuctions(String status) {

        return createSampleAuctions(status);

    }

    private static List<Auction> createSampleAuctions() {

        List<Auction> auctions = new ArrayList<>();
        int maxItems = generateRandomRangeInteger(1, 5);
        for (int a = 0; a < maxItems; a++) {
            auctions.add(createSampleAuction());
        }
        return auctions;

    }

    private static List<Auction> createSampleAuctions(String status) {

        return createSampleAuctions().parallelStream().filter(auction -> auction.getStatus().equals(status))
                .collect(Collectors.toList());

    }

    public static Auction createSampleAuction() {
        String auctionId = generateUniqueAuctionId();
        Long startT = getModifiedTimestamp(System.currentTimeMillis(), generateRandomRangeInteger(0, 5));
        Long stopT = getModifiedTimestamp(System.currentTimeMillis(), generateRandomRangeInteger(5, 9));

        Auction auction = new Auction();
        auction.setAuctionId(auctionId);
        auction.setAuctionDescription("Any Sample Auction Description.AuctionId:" + auctionId);
        auction.setAuctionName("Old is Gold:" + auctionId);
        auction.setItems(getSampleItems(auctionId));
        auction.setStartTimestamp(startT);
        auction.setStopTimestamp(stopT);

        return auction;
    }

    private static Item getSampleItem(String auctionId) {

        String itemId = generateUniqueItemId();
        Item item = new Item();
        item.setItemId(itemId);
        item.setItemName("Glass");
        item.setItemDescription("Any Sample Item Description. ItemId:" + itemId);
        item.setItemCount(1);
        item.setItemStartPrice(generateRandomRangeDouble(34, 124));
        item.setAuctionId(auctionId);

        return item;
    }

    private static List<Item> getSampleItems(String auctionId) {
        List<Item> items = new ArrayList<>();
        int maxItems = generateRandomRangeInteger(1, 5);
        for (int a = 0; a < maxItems; a++) {
            items.add(getSampleItem(auctionId));
        }
        return items;
    }

    public static String generateUniqueAuctionId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static String generateUniqueItemId() {
        UUID uuid = UUID.randomUUID();
        String itemId = uuid.toString().replace("-", "");
        return itemId;
    }

    public static String generateUniqueBidId() {
        UUID uuid = UUID.randomUUID();
        String itemId = uuid.toString().replace("-", "");
        return itemId;
    }

    public static Integer generateRandomRangeInteger(Integer minimum, Integer maximum) {
        return generateRandomRangeDouble(minimum, maximum).intValue();
    }

    public static Double generateRandomRangeDouble(Integer minimum, Integer maximum) {
        Double random = (Math.random() * (maximum - minimum + 1) + minimum);
        return Precision.round(random, 2);
    }

    public static Long getModifiedTimestamp(Long timestamp, Integer day) {
        return timestamp + Long.valueOf(day * 24 * 60 * 60 * 1000);
    }

    public static UserEntity convertToUserEntity(UserRegistration userRegistration, PasswordEncoder passwordEncoder,
            boolean isAdmin, String createdByUser) {

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRegistration.getUserName());
        userEntity.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        userEntity.setRoles(isAdmin ? "ROLE_ADMIN" : "ROLE_USER");
        userEntity.setActive(Boolean.TRUE);
        userEntity.setFirstname(userRegistration.getFirstName());
        userEntity.setMiddlename(userRegistration.getMiddleName());
        userEntity.setLastname(userRegistration.getLastname());
        userEntity.setContactNumber(userRegistration.getContactNumber());
        userEntity.setAddress(userRegistration.getAddress());
        userEntity.setEmailId(userRegistration.getEmailId());
        userEntity.setOrgType(userRegistration.getOrgType());
        userEntity.setCreatedTime(System.currentTimeMillis());
        userEntity.setModifiedTime(System.currentTimeMillis());
        userEntity.setCreatedBy(createdByUser);

        return userEntity;
    }

    public static void createAndPopulateAuctionId(Auction auction) {

        String auctionId = generateUniqueAuctionId();
        auction.setAuctionId(auctionId);
        auction.getItems().stream().forEach(item -> {
            item.setAuctionId(auctionId);
            item.setItemId(generateUniqueItemId());
        });

    }

    public static void populateCurrentTime(Auction auction) {
        auction.setCreatedTimestamp(System.currentTimeMillis());
    }

    public static String calculateCronExpression(Long timestamp) {
        // Convert the timestamp to a ZonedDateTime in the IST time zone
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.of("Asia/Kolkata"));

        // Extract the individual components (year, month, day, hour, minute) from the ZonedDateTime
        int year = zonedDateTime.getYear();
        int month = zonedDateTime.getMonthValue();
        int day = zonedDateTime.getDayOfMonth();
        int hour = zonedDateTime.getHour();
        int minute = zonedDateTime.getMinute();

        // Create the cron expression
        String cronExpression = String.format("0 %d %d %d %d ?", minute, hour, day, month);

        return cronExpression;
    }

    public static void populateStartStopTime(Auction auction) {
        LocalTime lastSecond = LocalTime.of(16, 47, 0, 000_000_000); // 11:59:59.999999999
        auction.setStartTimestamp(Utility.getTimestampsFor(0,lastSecond));
        auction.setStopTimestamp(Utility.getTimestampsFor(2, lastSecond));
    }

    /*public static long[] getTimestampsForTomorrow() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalTime lastSecond = LocalTime.of(23, 59, 59, 999_999_999); // 11:59:59.999999999

        ZonedDateTime midnightTomorrow = ZonedDateTime.of(tomorrow, midnight, ZoneId.systemDefault());
        ZonedDateTime lastSecondTomorrow = ZonedDateTime.of(tomorrow, lastSecond, ZoneId.systemDefault());

        long midnightTimestamp = midnightTomorrow.toInstant().toEpochMilli();
        long lastSecondOfTomorrowTimestamp = lastSecondTomorrow.toInstant().toEpochMilli();

        return new long[]{midnightTimestamp, lastSecondOfTomorrowTimestamp};
    }*/

    public static long getTimestampsForTomorrowMidnight() {
        return getTimestampsFor(1, LocalTime.MIDNIGHT);
    }

    public static long getTimestampsForTomorrowEOD() {
        LocalTime lastSecond = LocalTime.of(23, 59, 59, 999_999_999); // 11:59:59.999999999
        return getTimestampsFor(1, lastSecond);
    }

    public static long getTimestampsFor(long plusDays, LocalTime secondsAhead) {
        LocalDate tomorrow = LocalDate.now().plusDays(plusDays);
        ZonedDateTime tomorrowSecAhead = ZonedDateTime.of(tomorrow, secondsAhead, ZoneId.systemDefault());
        return tomorrowSecAhead.toInstant().toEpochMilli();
    }

    public static long getTimestampsForTodayMidnight() {
        return getTimestampsForToday(0, LocalTime.MIDNIGHT);
    }

    public static long getTimestampsForTodayEOD() {
        LocalTime lastSecond = LocalTime.of(23, 59, 59, 999_999_999); // 11:59:59.999999999
        return getTimestampsForToday(0, lastSecond);
    }

    public static long getTimestampsForToday(long plusDays, LocalTime secondsAhead) {
        LocalDate today = LocalDate.now().plusDays(plusDays);
        ZonedDateTime todaySecAhead = ZonedDateTime.of(today, secondsAhead, ZoneId.systemDefault());
        return todaySecAhead.toInstant().toEpochMilli();
    }
}
