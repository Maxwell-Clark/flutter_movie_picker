import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import './components/modal.dart';
import './routes/VideoPlayer.dart';
import 'package:firebase_core/firebase_core.dart';
import 'firebase_options.dart';

bool shouldUseFirestoreEmulator = true;


Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Movie Picker',
      theme: ThemeData(
        primarySwatch: Colors.red,
        scaffoldBackgroundColor: Colors.grey[700]
      ),
      home: const MyHomePage(title: 'Movie Picker'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  CollectionReference movies = FirebaseFirestore.instance.collection("mostPopularMovies");
  final ButtonStyle elevatedButtonStyle =
  ElevatedButton.styleFrom(textStyle: const TextStyle(fontSize: 20));

  @override
  void initState(){
    super.initState();
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: FutureBuilder(
        future: movies.doc("Thor").get(),
        builder: (context,  AsyncSnapshot<DocumentSnapshot> snapshot) {
          if(snapshot.connectionState == ConnectionState.done) {
            print(snapshot.hasData);
            print(snapshot);
            return  Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Container(
                      width: 254,
                      height: 128,
                      child: Center(
                          child: Text(
                              "${snapshot.data!.get("fullTitle")}",
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
                    description: "${snapshot.data!.get("description")}",
                  ),
                  Modal(
                    key: UniqueKey(),
                    modalTitle: "Cast",
                    description: "${snapshot.data!.get("cast")}",
                  ),
                  Modal(
                    key: UniqueKey(),
                    modalTitle: "Ratings",
                    description: "${snapshot.data!.get("rating")}",
                  ),
                  ElevatedButton(
                    child: const Text("Trailer"),
                    style: elevatedButtonStyle,
                    onPressed: () {
                      Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context)=>  VideoPlayer(videoId: snapshot.data!.get("videoId")))
                      );
                    },
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
}

