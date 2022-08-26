import 'package:flutter/material.dart';
import 'package:youtube_player_flutter/youtube_player_flutter.dart';

class VideoPlayer extends StatefulWidget {
  final String videoId;
  const VideoPlayer({ required this.videoId}): super();

  @override
  _ModalState createState() => _ModalState();

}

class _ModalState extends State<VideoPlayer> {

  late YoutubePlayerController _controller;

  @override
  void initState() {
    _controller = YoutubePlayerController(
      initialVideoId: widget.videoId,
      flags: const YoutubePlayerFlags(
        autoPlay: true,
        mute: false,
      ),
    );
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    final ButtonStyle elevatedButtonStyle =
    ElevatedButton.styleFrom(textStyle: const TextStyle(fontSize: 20));

    return Center(
        child: YoutubePlayerBuilder(
            player:  YoutubePlayer(
              controller: _controller,
              showVideoProgressIndicator: true,
              onReady: () {
                print("player is ready");
            },
              onEnded: (data) {},
            ),
            builder: (context, player) {
              return Column(
                children: [
                  player,
                ],
              );
            },
          )
    );
  }
}