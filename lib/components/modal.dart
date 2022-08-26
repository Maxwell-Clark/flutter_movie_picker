import 'package:flutter/material.dart';

class Modal extends StatefulWidget {
  final String modalTitle;
  final String description;

  const Modal({required Key key, required this.modalTitle, required this.description}): super(key:key);

  @override
  _ModalState createState() => _ModalState();

}

class _ModalState extends State<Modal> {


  @override
  Widget build(BuildContext context) {
    final ButtonStyle elevatedButtonStyle =
    ElevatedButton.styleFrom(textStyle: const TextStyle(fontSize: 20));
    return Center(
        child: ElevatedButton(
          style: elevatedButtonStyle,
          child: Text(widget.modalTitle),
          onPressed: () {
            showModalBottomSheet(
                context: context,
                builder: (BuildContext context) {
                  return SizedBox(
                    // height: 700,
                      child: Center(
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: <Widget>[
                              Text(widget.description)
                            ],
                          )
                      )
                  );
                }
            );
          },
        )
    );
  }
}