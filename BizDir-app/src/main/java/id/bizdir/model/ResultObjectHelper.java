package id.bizdir.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hendry on 20/04/2015.
 */
public class ResultObjectHelper {

    public static ResultObject getResult(String jsonResult) {
        ResultObject result = new ResultObject();
        try {
            JSONObject jObj = new JSONObject(jsonResult);
            result.setStatus(jObj.getInt("status"));
            result.setMessage(jObj.getString("message"));
            result.setResult(jObj.getString("result"));
        } catch (JSONException e) {
            result.setStatus(0);
            result.setMessage(e.getMessage());
            result.setResult(jsonResult);
            e.printStackTrace();
        }
        return result;
    }
}
