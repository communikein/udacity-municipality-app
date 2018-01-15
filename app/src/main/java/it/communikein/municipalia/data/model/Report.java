package it.communikein.municipalia.data.model;

import android.support.annotation.NonNull;

import java.util.Map;

public class Report extends News {

    private static final String ARG_SUBMITTER_ID = "submitter_id";

    @NonNull
    private String submitterId;

    public Report(@NonNull String id, @NonNull String submitterId, String title, String description,
                  long timestamp) {
        super(id, title, description, null, timestamp);

        setSubmitterId(submitterId);
    }

    public Report(String id, Map<String, Object> map) {
        super(id, map);

        if (map.containsKey(ARG_SUBMITTER_ID))
            setSubmitterId((String) map.get(ARG_SUBMITTER_ID));
    }


    public String getSubmitterId() {
        return this.submitterId;
    }

    public void setSubmitterId(@NonNull String submitterId) {
        this.submitterId = submitterId;
    }
}
