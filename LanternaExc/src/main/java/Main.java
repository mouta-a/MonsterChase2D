import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.util.Random;


public class Main {

    public static void main(String[] args) throws Exception {

        // Ted testar

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        Position[] walls = {new Position(5, 7), new Position(10, 4), new Position(12, 12),new Position(38,7),new Position(20,15),new Position(30,10),
                new Position(25,13),new Position(40,20),new Position(60,3),new Position(50,20),new Position(60,10), new Position(70,15),new Position(35,19),new Position(25,10),
                new Position(23,17), new Position(60,15), new Position(23,4),new Position(30,2), new Position(25,5),new Position(40,17),new Position(45,8)};
        Position[] longWall = new Position[10];


        boolean continueReadingInput = true;
        boolean spawnNew = false;
        terminal.setCursorVisible(false);
        int x = 10;
        int y = 10;
        final char player = '\u263b';
        final char block = '\u2588';
        int counter = 0;

        //Poäng räknare
        String counterS = "Points: "+counter;
        for (int i = 0; i < counterS.length(); i++) {
            terminal.setForegroundColor(TextColor.ANSI.GREEN);
            terminal.setCursorPosition(i + 70, 0);
            terminal.putCharacter(counterS.charAt(i));
        }

        //Ritar ut karaktären
        terminal.setForegroundColor(TextColor.ANSI.YELLOW);
        terminal.setCursorPosition(x, y);
        terminal.putCharacter(player);
        terminal.setForegroundColor(TextColor.ANSI.WHITE);

        //Skapar en lång vägg
        for (int i = 0; i < longWall.length; i++) {
                longWall[i] = new Position((55 + i), 17);
                terminal.setCursorPosition(longWall[i].x, longWall[i].y);
                terminal.putCharacter(block);
        }


        for (Position p : walls) {
            terminal.setCursorPosition(p.x, p.y);
            terminal.putCharacter(block);
        }
        terminal.setForegroundColor(TextColor.ANSI.RED);
        Random r = new Random();
        int ranX = r.nextInt(39);
        int ranY = r.nextInt(2,20);
        Position[] bombPosition = {new Position(ranX, ranY), new Position(ranX + 1, ranY), new Position(ranX, ranY + 1), new Position(ranX + 1, ranY + 1)};
        for (Position b : bombPosition) {
            terminal.setCursorPosition(b.x, b.y);
            terminal.putCharacter('\u29F0');
        }

        r = new Random();
        ranX = r.nextInt(40,80);
        ranY = r.nextInt(2,20);
        Position[] bombPosition2 = {new Position(ranX, ranY), new Position(ranX + 1, ranY), new Position(ranX, ranY + 1), new Position(ranX + 1, ranY + 1)};
        for (Position b : bombPosition2) {
            terminal.setCursorPosition(b.x, b.y);
            terminal.putCharacter('\u29F0');
        }

        terminal.setForegroundColor(TextColor.ANSI.GREEN);
        r = new Random();
        ranX = r.nextInt(80);
        ranY = r.nextInt(20);
        Position[] winPosition = {new Position(ranX, ranY), new Position(ranX + 1, ranY), new Position(ranX, ranY + 1), new Position(ranX + 1, ranY + 1)};
        for (Position wP : winPosition) {
            terminal.setCursorPosition(wP.x, wP.y);
            terminal.putCharacter('\u2662');
        }
        terminal.setForegroundColor(TextColor.ANSI.WHITE);

        Monster monster = new Monster('\u1F60', new Position(1, 1));
        Monster monster2 = new Monster('\u1F60', new Position(20, 20));
        Monster monster3 = new Monster('\u1F60', new Position(60, 10));

        Monster[] monsters = {monster, monster2, monster3};

        for (Monster m : monsters) {
            terminal.setForegroundColor(TextColor.ANSI.CYAN);
            terminal.setCursorPosition(m.position.x, m.position.y);
            terminal.putCharacter(m.type);
        }


        terminal.flush();

        while (continueReadingInput) {

            spawnNew = false;
            counterS = "Points: "+counter;
            for (int i = 0; i < counterS.length(); i++) {
                terminal.setForegroundColor(TextColor.ANSI.GREEN);
                terminal.setCursorPosition(i + 70, 0);
                terminal.putCharacter(counterS.charAt(i));
            }

            KeyStroke keyStroke = null;
            do {
                Thread.sleep(5); // might throw InterruptedException
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);

            KeyType type = keyStroke.getKeyType();
            Character c = keyStroke.getCharacter();

            if (c == Character.valueOf('q')) {
                continueReadingInput = false;
                System.out.println("quit");
                terminal.close();
            }

            int oldX = x;
            int oldY = y;


            switch (type) {
                case ArrowDown:
                    y += 2;
                    break;
                case ArrowUp:
                    y -= 2;
                    break;

                case ArrowLeft:
                    x -= 2;
                    break;

                case ArrowRight:
                    x += 2;
                    break;
            }


            for (Monster m : monsters) {
                boolean monsterCrash = false;

                int monsterOldX = m.position.x;
                int monsterOldY = m.position.y;

                int diffX = m.position.x - x;
                int absDiffX = Math.abs(diffX);
                int diffY = m.position.y - y;
                int absDiffY = Math.abs(diffY);

                if (absDiffX > absDiffY) {

                    if (diffX < 0) {
                        m.position.x += 1;
                    } else {
                        m.position.x -= 1;
                    }
                } else if (absDiffX < absDiffY) {

                    if (diffY < 0) {
                        m.position.y += 1;
                    } else {
                        m.position.y -= 1;
                    }
                } else {

                    if (diffX < 0) {
                        m.position.x += 1;
                    } else {
                        m.position.x -= 1;
                    }
                    if (diffY < 0) {
                        m.position.y += 1;
                    } else {
                        m.position.y -= 1;
                    }
                }

                for (Position p : walls) {
                    if (p.x == m.position.x && p.y == m.position.y) {
                        monsterCrash = true;
                    }
                }

                for (Position p : longWall) {
                    if (p.x == m.position.x && p.y == m.position.y) {
                        monsterCrash = true;
                    }
                }

                if (m.position.x == x && m.position.y == y) {
                    String message = "*** GAME OVER ***";
                    String message2 = "*** You only scored:"+counter+" points ***";
                    for (int i = 0; i < message.length(); i++) {
                        terminal.setForegroundColor(TextColor.ANSI.RED);
                        terminal.setCursorPosition(i + 33, 11);
                        terminal.putCharacter(message.charAt(i));
                    }
                    for(int i = 0;i<message2.length(); i++){
                        terminal.setForegroundColor(TextColor.ANSI.RED);
                        terminal.setCursorPosition(i + 25, 12);
                        terminal.putCharacter(message2.charAt(i));
                    }
                    continueReadingInput = false;
                    //System.out.println("quit");
                    //terminal.close();
                }

                if (monsterCrash) {
                    m.position.x = monsterOldX;
                    m.position.y = monsterOldY;
                } else {
                    terminal.setCursorPosition(monsterOldX, monsterOldY);
                    terminal.putCharacter(' ');
                    terminal.setForegroundColor(TextColor.ANSI.CYAN);
                    terminal.setCursorPosition(m.position.x, m.position.y);
                    terminal.putCharacter(m.type);

                }

            }



            boolean crash = false;

            for (Position p : walls) {
                if (p.x == x && p.y == y) {
                    crash = true;
                }
            }

            for (Position p : longWall) {
                if (p.x == x && p.y == y) {
                    crash = true;
                }
            }

            for (Position b : bombPosition) {
                if (b.x == x && b.y == y) {
                    String message = "*** GAME OVER ***";
                    String message2 = "*** You only scored:"+counter+" points ***";
                    for (int i = 0; i < message.length(); i++) {
                        terminal.setForegroundColor(TextColor.ANSI.RED);
                        terminal.setCursorPosition(i + 33, 11);
                        terminal.putCharacter(message.charAt(i));
                    }
                    for(int i = 0;i<message2.length(); i++){
                        terminal.setForegroundColor(TextColor.ANSI.RED);
                        terminal.setCursorPosition(i + 25, 12);
                        terminal.putCharacter(message2.charAt(i));
                    }
                    continueReadingInput = false;
                }
            }

            for (Position b : bombPosition2) {
                if (b.x == x && b.y == y) {
                    String message = "*** GAME OVER ***";
                    String message2 = "*** You only scored:"+counter+" points ***";
                    for (int i = 0; i < message.length(); i++) {
                        terminal.setForegroundColor(TextColor.ANSI.RED);
                        terminal.setCursorPosition(i + 33, 11);
                        terminal.putCharacter(message.charAt(i));
                    }
                    for(int i = 0;i<message2.length(); i++){
                        terminal.setForegroundColor(TextColor.ANSI.RED);
                        terminal.setCursorPosition(i + 25, 12);
                        terminal.putCharacter(message2.charAt(i));
                    }
                    continueReadingInput = false;
                }
            }

            for (Position wP : winPosition) {
                if (wP.x == x && wP.y == y) {
                    counter ++;
                    spawnNew = true;
                }
            }

            if(counter ==10){
                String message = "*** WINNER WINNER CHICKEN DINNER!!!!! ***";
                    for (int i = 0; i < message.length(); i++) {
                        terminal.setForegroundColor(TextColor.ANSI.GREEN);
                        terminal.setCursorPosition(i + 22, 10);
                        terminal.putCharacter(message.charAt(i));
                    }
                    continueReadingInput = false;
            }

            if(spawnNew){
                for (Position wP : winPosition) {
                    terminal.setCursorPosition(wP.x, wP.y);
                    terminal.putCharacter(' ');
                }
                terminal.setForegroundColor(TextColor.ANSI.GREEN);
                r = new Random();
                ranX = r.nextInt(80);
                ranY = r.nextInt(2,20);
                winPosition = new Position[]{new Position(ranX, ranY), new Position(ranX + 1, ranY), new Position(ranX, ranY + 1), new Position(ranX + 1, ranY + 1)};
                for (Position wP : winPosition) {
                    terminal.setCursorPosition(wP.x, wP.y);
                    terminal.putCharacter('\u2662');
                }
            }



                if (crash) {
                    x = oldX;
                    y = oldY;
                } else {
                    terminal.setCursorPosition(oldX, oldY);
                    terminal.putCharacter(' ');
                    terminal.setForegroundColor(TextColor.ANSI.YELLOW);
                    terminal.setCursorPosition(x, y);
                    terminal.putCharacter(player);
                    terminal.setForegroundColor(TextColor.ANSI.WHITE);

                }
                terminal.flush();
            }

        }
    }


