//k_5v6n6ipg
var axios = require('axios');
const { initializeApp, applicationDefault, cert } = require('firebase-admin/app');
const { getFirestore, Timestamp, FieldValue } = require('firebase-admin/firestore');


let top250Movies = []
let top250TV = []
let mostPopularMovies = []
let mostPopularTV = []

const getMovieTitles = async function() {
    try {
        // let request = await axios.get('https://imdb-api.com/en/API/Top250Movies/k_5v6n6ipg');
        // top250Movies = request.data.items;
        //
        // let request = await axios.get('https://imdb-api.com/en/API/Top250TVs/k_5v6n6ipg')
        // request.data.items.forEach((TV) => top250TV.push(TV));
        // top250TV = request.data.items;
        //
        // request = await axios.get("https://imdb-api.com/en/API/MostPopularMovies/k_5v6n6ipg")
        // mostPopularMovies = request.data.items;

        request = await axios.get("https://imdb-api.com/en/API/MostPopularTVs/k_5v6n6ipg")
        // request.data.items.forEach((TV) => mostPopularTV.push(TV));
        mostPopularTV = request.data.items;
        // console.log("top movies "+ top250Movies)
    } catch (e) {
        console.error("Error: " + e)
    }
}
const saveTitles = async function() {
    try {
        initializeApp({
            credential: cert('./creds/movie-picker-6f264-firebase-adminsdk-gj7h5-9bd0890625.json')
        });
        const db = getFirestore();
        const topMovies = db.collection('top250Movies')
        const topTV = db.collection('top250TV');
        const popularMovies = db.collection('mostPopularMovies');
        const popularTV = db.collection('mostPopularTV')

        console.log(top250Movies);
        let counter = 0

        // for (const movie of top250Movies) {
        //     await topMovies.doc(`${counter}`).set({
        //         title: movie.title,
        //         year: movie.year,
        //         fullTitle: movie.fullTitle,
        //         image: movie.image,
        //         crew: movie.crew,
        //         imdbID: movie.id
        //     });
        //     counter = counter + 1;
        //     console.log("added " + movie)
        // }
        // for (const tv of top250TV) {
        //     await topTV.doc(`${counter}`).set({
        //         title: tv.title,
        //         year: tv.year,
        //         fullTitle: tv.fullTitle,
        //         image: tv.image,
        //         crew: tv.crew,
        //         imdbID: tv.id
        //     })
        //     console.log("added " + tv)
        //     counter = counter+1
        // }
        // for (const movie of mostPopularMovies) {
        //     await popularMovies.doc(movie.title).set({
        //         title: movie.title,
        //         year: movie.year,
        //         fullTitle: movie.fullTitle,
        //         image: movie.image,
        //         crew: movie.crew,
        //         imdbID: movie.id
        //     });
        //     counter = counter + 1
        //     console.log("added " + movie)
        // }
        //
        for (const tv of mostPopularTV) {
            await popularTV.doc(tv.title).set({
                title: tv.title,
                year: tv.year,
                fullTitle: tv.fullTitle,
                image: tv.image,
                crew: tv.crew,
                imdbID: tv.id
            })
            console.log("added tv" + tv)
            counter = counter + 1;
        }

    } catch (e) {
        console.error("Error: " + e)
    }

}

const run = async function() {
    await getMovieTitles();
    await saveTitles();
}
run()

// we will make a collection of movie titles
// a top 250 movie collection, top 250 tv, most popular movies, most popular tv