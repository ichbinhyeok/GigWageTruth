package com.gigwager.model;

public record DriverReportSubmission(
        String submittedAt,
        String sourcePage,
        String sourcePath,
        String app,
        String appName,
        String city,
        String citySlug,
        String modeledNetHourly,
        String weeklyGross,
        String weeklyMiles,
        String weeklyHours,
        String activeTime,
        String deliveryCount,
        String shiftWindow,
        String driverNote,
        String editorialStatus) {
}
