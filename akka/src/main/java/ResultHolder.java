import model.MasterResponse;

class ResultHolder {

    private MasterResponse response;

    MasterResponse getResult() {
        return response;
    }

    void setResult(MasterResponse response) {
        this.response = response;
    }
}