package id.bizdir.list;

import java.util.List;

import id.bizdir.model.Association;

/**
 * Created by Hendry on 19/09/2015.
 */
public class AssociationList {

    private int status;
    private String message;
    private List<Association> result;

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

    public List<Association> getResult() {
        return result;
    }

    public void setResult(List<Association> result) {
        this.result = result;
    }
}
