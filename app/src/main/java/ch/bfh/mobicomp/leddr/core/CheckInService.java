package ch.bfh.mobicomp.leddr.core;

import retrofit.http.GET;

public interface CheckInService {

    @GET(Constants.Http.URL_CHECKINS_FRAG)
    CheckInWrapper getCheckIns();
}
