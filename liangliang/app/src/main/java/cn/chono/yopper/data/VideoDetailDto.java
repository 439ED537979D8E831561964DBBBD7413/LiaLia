package cn.chono.yopper.data;

/**
 * Created by SQ on 2015/10/19.
 */
public class VideoDetailDto {

//"#viewStatus": "MyVideo_NotReady = 0,  MyVideo_InAuditing = 1, MyVideo_Success = 2,UserVideo_OK_C1 = 3,   UserVideo_NotOpen_C2 = 4,
// UserVideo_VisitorNotReady_C3 = 5,UserVideo_VisitorNotOpen_C4 = 6,UserVideo_BothNotReady_C5_1 = 7, UserVideo_NotReady_C5_2 = 8，UserVideo_Exceed = 9

    private int  viewStatus;
    private VerificationDto verification;

    public int getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(int viewStatus) {
        this.viewStatus = viewStatus;
    }

    public VerificationDto getVerification() {
        return verification;
    }

    public void setVerification(VerificationDto verification) {
        this.verification = verification;
    }
}