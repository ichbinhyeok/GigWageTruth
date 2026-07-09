package com.gigwager.service;

import com.gigwager.model.DriverReportSubmission;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class DriverReportSubmissionService {

    private static final String HEADER = String.join(",",
            "submitted_at",
            "source_page",
            "source_path",
            "app",
            "app_name",
            "city",
            "city_slug",
            "modeled_net_hourly",
            "weekly_gross",
            "weekly_miles",
            "weekly_hours",
            "active_time",
            "delivery_count",
            "shift_window",
            "driver_note",
            "editorial_status") + "\n";

    private final List<DriverReportSubmission> pendingSubmissions = new CopyOnWriteArrayList<>();
    private final Path submissionPath;

    public DriverReportSubmissionService() {
        this(resolveSubmissionPath());
    }

    DriverReportSubmissionService(Path submissionPath) {
        this.submissionPath = submissionPath;
    }

    public DriverReportSubmission savePending(Map<String, String> form) {
        DriverReportSubmission submission = new DriverReportSubmission(
                Instant.now().toString(),
                clean(form.get("source_page"), 80),
                clean(form.get("source_path"), 160),
                clean(form.get("app"), 32),
                clean(form.get("app_name"), 32),
                clean(form.get("city"), 80),
                clean(form.get("city_slug"), 80),
                clean(form.get("modeled_net_hourly"), 32),
                clean(form.get("weekly_gross"), 32),
                clean(form.get("weekly_miles"), 32),
                clean(form.get("weekly_hours"), 32),
                clean(form.get("active_time"), 32),
                clean(form.get("delivery_count"), 32),
                clean(form.get("shift_window"), 80),
                clean(form.get("driver_note"), 500),
                "pending_editorial_review");

        pendingSubmissions.add(submission);
        appendSubmission(submission);
        return submission;
    }

    public int pendingCount() {
        return pendingSubmissions.size();
    }

    private void appendSubmission(DriverReportSubmission submission) {
        try {
            Path parent = submissionPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(submissionPath)) {
                Files.writeString(submissionPath, HEADER, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            }
            Files.writeString(submissionPath, toCsvRow(submission), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ignored) {
            // Keep the in-memory review queue available when the runtime filesystem is read-only.
        }
    }

    private String toCsvRow(DriverReportSubmission submission) {
        return String.join(",",
                csv(submission.submittedAt()),
                csv(submission.sourcePage()),
                csv(submission.sourcePath()),
                csv(submission.app()),
                csv(submission.appName()),
                csv(submission.city()),
                csv(submission.citySlug()),
                csv(submission.modeledNetHourly()),
                csv(submission.weeklyGross()),
                csv(submission.weeklyMiles()),
                csv(submission.weeklyHours()),
                csv(submission.activeTime()),
                csv(submission.deliveryCount()),
                csv(submission.shiftWindow()),
                csv(submission.driverNote()),
                csv(submission.editorialStatus())) + "\n";
    }

    private static Path resolveSubmissionPath() {
        String override = System.getenv("DRIVER_REPORT_SUBMISSION_PATH");
        if (override != null && !override.isBlank()) {
            return Path.of(override);
        }
        return Path.of("build", "driver-report-submissions", "pending-driver-reports.csv");
    }

    private static String clean(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        String cleaned = value
                .replace('\r', ' ')
                .replace('\n', ' ')
                .replace('\t', ' ')
                .trim();
        if (cleaned.length() > maxLength) {
            return cleaned.substring(0, maxLength);
        }
        return cleaned;
    }

    private static String csv(String value) {
        String safe = value == null ? "" : value.replace("\"", "\"\"");
        return "\"" + safe + "\"";
    }
}
