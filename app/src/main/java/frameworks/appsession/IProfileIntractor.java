package frameworks.appsession;
public interface IProfileIntractor {

    public void getProfile(onProfileDownloaded listener);
    public interface onProfileDownloaded {
        public void onProfileDownloadSuccessfully(UserInfo userInfo);
        public void onProfileDowloadFail();
    }

    public interface onProfileUploaded {
        public void onProfileUploadedSuccessfully();
        public void onProfileUploadFail();
    }
}