package com.example.android.progressplayer.model

import io.reactivex.Observable

class TrackExample {

    fun getTrackList(): Observable<TrackModel> {
        return Observable.just(
            TrackModel("Don't Stay", "Linkin Park", 200),
            TrackModel("Smack my Bitch up", "Prodigy", 210),
            TrackModel("Fuel frei", "Ramstein", 205),
            TrackModel("Get Up", "Korn feat. Skrillex", 190)
        )
    }
}