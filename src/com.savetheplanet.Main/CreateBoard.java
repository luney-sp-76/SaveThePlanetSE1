package com.savetheplanet.Main;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

final class CreateBoard {

    public CreateBoard() {}

    static List<Square> createBoard() {

        List<Square> board = new ArrayList<>();

        try {
            List<String> fields = Files.lines(Path.of("fields.txt")).toList();
            Stream<String> squares = Files.lines(Path.of("squares.txt"));

            squares.forEach(line -> {

                String[] arr = line.split(",");
                String name = arr[0];
                int field = Integer.parseInt(arr[1]);

                if (field >= 3) {
                    board.add(new FundableSquare(name, field, fields.get(Integer.parseInt(arr[1])).split(",")));
                } else {
                    board.add(new Square(arr[0], Integer.parseInt(arr[1])));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return board;
    }
}