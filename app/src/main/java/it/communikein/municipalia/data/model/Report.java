package it.communikein.municipalia.data.model;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

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

    public Report(JSONObject json) throws JSONException {
        super(json);

        if (json.has(ARG_SUBMITTER_ID))
            setSubmitterId(json.getString(ARG_SUBMITTER_ID));
    }

    public Report(String id, Map<String, Object> map) {
        super(id, map);

        if (map.containsKey(ARG_SUBMITTER_ID))
            setSubmitterId((String) map.get(ARG_SUBMITTER_ID));
    }

    public String getSubmitterId() {
        return this.submitterId;
    }

    public void setSubmitterId(String submitterId) {
        this.submitterId = submitterId;
    }


    public JSONObject toJSON() {
        JSONObject obj = super.toJSON();

        try {
            obj.put(ARG_SUBMITTER_ID, getSubmitterId());
        } catch (JSONException e){
            obj = new JSONObject();
        }

        return obj;
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }
}
