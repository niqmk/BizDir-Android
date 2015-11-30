package id.bizdir.list;

import java.util.List;

import id.bizdir.model.Anggota;

/**
 * Created by Hendry on 19/09/2015.
 */
public class AssociationMemberList {

    private int status;
    private String message;
    private List<Anggota> result;

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

    public List<Anggota> getResult() {
        return result;
    }

    public void setResult(List<Anggota> result) {
        this.result = result;
    }
}
