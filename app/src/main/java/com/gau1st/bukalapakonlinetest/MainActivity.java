package com.gau1st.bukalapakonlinetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Server server = null;
    private ImageView chessBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onResume() {
        startServer();
        super.onResume();
    }

    @Override
    public void onPause() {
        stopServer();
        super.onPause();
    }

    private void startServer() {
        server = new Server("xinuc.org", 7387);
        server.setListener(new Server.OnReadListener() {
            @Override
            public void onRead(Server serverThread, final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // reset chess board to empty
                        resetChessBoard();

                        String[] position = response.split("\\s+");
                        for (String aPosition : position) {

                            //get chess block position (ID) and set it as image
                            String chessBlockPosition = aPosition.substring(1, 3);
                            int chessBlockID = getResources().getIdentifier(chessBlockPosition, "id", getPackageName());
                            chessBlock = (ImageView) findViewById(chessBlockID);


                            //get chess piece name from piece initial and insert it to chess block
                            String pieceInitial = aPosition.substring(0, 1);
                            String pieceName = getPieceName(pieceInitial);
                            int file = getResources().getIdentifier(pieceName, "drawable", getPackageName());
                            chessBlock.setImageResource(file);

                        }
                    }
                });
            }
        });
        server.start();
    }

    private String getPieceName(String pieceInitial){
        String result = null;

        switch(pieceInitial){
            case "b":
                result = "bishop_black";
                break;
            case "B":
                result = "bishop_white";
                break;
            case "k":
                result = "king_black";
                break;
            case "K":
                result = "king_white";
                break;
            case "n":
                result = "knight_black";
                break;
            case "N":
                result = "knight_white";
                break;
            case "q":
                result = "queen_black";
                break;
            case "Q":
                result = "queen_white";
                break;
            case "r":
                result = "rook_black";
                break;
            case "R":
                result = "rook_white";
                break;
            default:
                break;
        }
        return result;
    }

    private void resetChessBoard(){
        for(int i=1;i<=8;i++){
            for(int j=0;j<8;j++){
                String position = Character.toString((char)(j+97))+String.valueOf(i);
                int chessBlockID = getResources().getIdentifier(position, "id", getPackageName());
                chessBlock = (ImageView) findViewById(chessBlockID);
                chessBlock.setImageResource(android.R.color.transparent);
            }
        }
    }

    private void stopServer() {
        if (server != null) {
            try {
                server.socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
