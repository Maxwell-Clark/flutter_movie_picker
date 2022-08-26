# -*- coding: utf-8 -*-

# Sample Python code for youtube.search.list
# See instructions for running these code samples locally:
# https://developers.google.com/explorer-help/code-samples#python

import os

import google_auth_oauthlib.flow
import googleapiclient.discovery
import googleapiclient.errors

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

scopes = ["https://www.googleapis.com/auth/youtube.force-ssl"]

cred = credentials.Certificate("../javascript/creds/movie-picker-6f264-firebase-adminsdk-gj7h5-9bd0890625.json")
mostPopularMovieTitles = []
mostPopularMovieFullTitles = []
mostPopularTVTitles = []
topMovieTitles = []
topTVTitles = []


def main():
    # Disable OAuthlib's HTTPS verification when running locally.
    # *DO NOT* leave this option enabled in production.
    os.environ["OAUTHLIB_INSECURE_TRANSPORT"] = "1"

    api_service_name = "youtube"
    api_version = "v3"
    client_secrets_file = "../javascript/creds/youtubeAPICreds.json"

    firebase_admin.initialize_app(cred)

    # Get credentials and create an API client
    flow = google_auth_oauthlib.flow.InstalledAppFlow.from_client_secrets_file(
        client_secrets_file, scopes)
    credentials = flow.run_local_server()
    youtube = googleapiclient.discovery.build(
        api_service_name, api_version, credentials=credentials)

    db = firestore.client()
    # handleTopMovies(db, youtube)
    # handleMostPopularMovies(db, youtube)
    # handleTopTV(db, youtube)
    handleMostPopularTV(db, youtube)
    print("movie titles")
    print(mostPopularMovieTitles)

    # for title in movie_list:
    #     request = youtube.search().list(
    #         part="snippet",
    #         q=title + " trailer"
    #     )
    #     response = request.execute()
    #     videoId = response.get("items")[0].get("id").get("videoId")
    #     print(videoId)
    #     # db.collection("movies").document(u + title).update({"videoId": videoId})
    #     db.collection("movies").document(u'testingnow').set({u'hello': u'world'})

    # add to the record containing the matching movie name


def handleMostPopularMovies(db, youtube):
    mostPopularMovies = db.collection(u'mostPopularMovies');
    mostPopularMoviesDocs = mostPopularMovies.stream()
    for movie in mostPopularMoviesDocs:
        movieToAdd = movie.to_dict()
        print(movieToAdd["fullTitle"])
        mostPopularMovieFullTitles.append(movieToAdd["fullTitle"])
        mostPopularMovieTitles.append(movieToAdd["title"])

    for title in mostPopularMovieTitles:
        request = youtube.search().list(
            part="snippet",
            q=title + " trailer"
        )
        response = request.execute()
        videoId = response.get("items")[0].get("id").get("videoId")
        print(videoId)
        mostPopularMovies.document(u'%s' % title).update({"videoId": videoId})


def handleMostPopularTV(db, youtube):
    mostPopularTV = db.collection(u'mostPopularTV');
    mostPopularTVDocs = mostPopularTV.stream()
    for TV in mostPopularTVDocs:
        tvToAdd = TV.to_dict()
        print(tvToAdd["fullTitle"])
        mostPopularTVTitles.append(tvToAdd["title"])
        #got to This is us
    afterThisIsUs = False
    for title in mostPopularTVTitles:
        if title == "This Is Us":
            afterThisIsUs = True
        if afterThisIsUs:
            request = youtube.search().list(
                part="snippet",
                q=title + " trailer"
            )
            response = request.execute()
            videoId = response.get("items")[0].get("id").get("videoId")
            print(videoId)
            mostPopularTV.document(u'' + title).update({"videoId": videoId})


def handleTopMovies(db, youtube):
    topMovies = db.collection(u'top250Movies')
    topMovieDocs = topMovies.stream()
    counter = 0
    for movie in topMovieDocs:
        movieToAdd = movie.to_dict()
        print(movieToAdd["fullTitle"])
        topMovieTitles.append(movieToAdd["fullTitle"])
        topMovies.document(u'0').update({"videoId": "testing"})
        counter = counter + 1


def handleTopTV(db, youtube):
    topTV = db.collection(u'top250TV')
    topTVDocs = topTV.stream()
    for TV in topTVDocs:
        tvToAdd = TV.to_dict()
        print(tvToAdd["fullTitle"])
        topTVTitles.append(tvToAdd["fullTitle"])


if __name__ == "__main__":
    main()
