package id.bizdir.model;

import java.util.List;

/**
 * Created by Hendry on 07/10/2015.
 */
public class NewstickerResult {
    private int status;
    private String message;
    private List<Newsticker> result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Newsticker> getResult() {
        return result;
    }

    public void setResult(List<Newsticker> result) {
        this.result = result;
    }
}
