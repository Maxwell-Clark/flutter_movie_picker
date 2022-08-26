import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import './components/modal.dart';
import 'package:firebase_core/firebase_core.dart';
import 'firebase_options.dart';

bool shouldUseFirestoreEmulator = true;


Future<void> main() async {
  // WidgetsFlutterBinding.ensureInitialized();
  // await Firebase.initializeApp();

  // await Firebase.initializeApp(
  //   options: DefaultFirebaseOptions.currentPlatform,
  // );
  // if (shouldUseFirestoreEmulator) {
  //   FirebaseFirestore.instance.useFirestoreEmulator('localhost', 8080);
  // }

  runApp(const MyApp());
}

final moviesRef = FirebaseFirestore.instance
    .collection('mostPopularMovies')
    .withConverter<Media>(
  fromFirestore: (snapshots, _) => Media.fromJson(snapshots.data()!),
  toFirestore: (movie, _) => movie.toJson(),
);

Media testingMedia = moviesRef.doc('Dune') as Media;



class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Movie Picker',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.red,
        scaffoldBackgroundColor: Colors.grey[700]
      ),
      home: const MyHomePage(title: 'Movie Picker'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  void initState(){
    super.initState();
  }
  // add methods here
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // appBar: AppBar(
      //
      //   title: Text(widget.title),
      // ),
      body: FutureBuilder(
        future: getData(),
        builder: (context, AsyncSnapshot<DocumentSnapshot> snapshot) {
          if(snapshot.connectionState == ConnectionState.done) {
            print(snapshot.hasData);
            return  Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Container(
                      width: 254,
                      height: 128,
                      child: Center(
                          child: Text(
                              "${snapshot}",
                              style: const TextStyle(
                                  fontWeight: FontWeight.bold,
                                  color: Colors.white,
                                  fontSize: 30
                              )
                          )
                      )
                  ),
                  const SizedBox(height: 30),
                  Modal(
                    key: UniqueKey(),
                    modalTitle: "Description",
                    description: "This is the description of the movie",
                  ),
                  Modal(
                    key: UniqueKey(),
                    modalTitle: "Cast",
                    description: "This is the cast of the movie",
                  ),
                  Modal(
                    key: UniqueKey(),
                    modalTitle: "Ratings",
                    description: "This is the ratings of the movie",
                  ),
                  Modal(
                    key: UniqueKey(),
                    modalTitle: "Trailer",
                    description: "This is the trailer of the movie",
                  ),

                  const SizedBox(height: 50),
                  ButtonBar(
                    alignment: MainAxisAlignment.center,
                    children: <Widget>[
                      IconButton(
                        icon: const Icon(
                            Icons.favorite,
                            color: Colors.redAccent
                        ),
                        onPressed: () {
                          print('Favorite');
                        },
                      ),
                      IconButton(
                        icon: const Icon(
                            Icons.thumb_up,
                            color: Colors.blueAccent
                        ),
                        onPressed: () {
                          print("like");
                        },
                      ),
                      IconButton(
                        icon: const Icon(
                          Icons.thumb_down,
                          color: Colors.blueAccent,
                        ),

                        onPressed: () {
                          print("dislike");
                        },
                      ),
                    ],
                  ),
                ],
              ),
            );
            return Text(
              "data not found"
            );
          }
          else if (snapshot.connectionState == ConnectionState.none) {
            return Text("No Data");
          }
          return const Center(
              child: CircularProgressIndicator()
          );
        },
      )
    );
  }

  Future<DocumentSnapshot> getData() async {
    await Firebase.initializeApp();
    final moviesRef = FirebaseFirestore.instance
        .collection('mostPopularMovies')
        .withConverter<Media>(
      fromFirestore: (snapshots, _) => Media.fromJson(snapshots.data()!),
      toFirestore: (movie, _) => movie.toJson(),
    );
    print("help");
    print(moviesRef.doc('Thor').get());
    return await FirebaseFirestore.instance
        .collection("mostPopularMovies")
        .doc("Dune")
        .get();
  }
}

@immutable
class Media {
  const Media({
    required this.cast,
    required this.crew,
    required this.description,
    required this.fullTitle,
    required this.image,
    required this.imdbID,
    required this.rating,
    required this.title,
    required this.videoId,
    required this.year
  });

  Media.fromJson(Map<String, Object?> json)
      : this(
    cast: (json['cast']! as String),
    crew: json['crew']! as String,
    description: json['description']! as String,
    fullTitle: json['fullTitle']! as String,
    image: json['image']! as String,
    imdbID: json['imdbID']! as String,
    rating: json['rating']! as String,
    title: json['title']! as String,
    videoId: json['videoId']! as String,
    year: json['year']! as String,
  );

  final String cast;
  final String crew;
  final String title;
  final String year;
  final String description;
  final String rating;
  final String imdbID;
  final String fullTitle;
  final String image;
  final String videoId;

  Map<String, Object?> toJson() {
    return {
      'cast': cast,
      'crew': crew,
      'description': description,
      'fullTitle': fullTitle,
      'image': image,
      'imdbID': imdbID,
      'rating': rating,
      'title': title,
      'videoId': videoId,
      'year': year,
    };
  }
}
