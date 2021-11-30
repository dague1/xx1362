import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class Leona {

    private class Vec2{
        public Vec2(){}
        public Vec2(Vec2 other){
            x = other.x;
            y = other.y;
        }
        Double x = 0.0, y = 0.0;
    }

    private ArrayList<String> positionHistory = new ArrayList<>();

    private double direction;
    private Vec2 position = new Vec2();

    private int color = 0x0000ff;

    private boolean record = false;

    public void Move(Integer units){
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
        unusualSymbols.setDecimalSeparator('.');

        DecimalFormat formater = new DecimalFormat("#0.0000");
        formater.setDecimalFormatSymbols(unusualSymbols);

        String prevPosX = formater.format(position.x).replaceAll( "^−(?=0(\\.0*)?$)", "");
        String prevPosY = formater.format(position.y).replaceAll( "^−(?=0(\\.0*)?$)", "");

        position.x += units * Math.cos(Math.PI * direction / 180.0);
        position.y += units * Math.sin(Math.PI * direction / 180.0);

        String posX = formater.format(position.x).replaceAll( "^−(?=0(\\.0*)?$)", "");
        String posY = formater.format(position.y).replaceAll( "^−(?=0(\\.0*)?$)", "");

        String HexColor = String.format("%06X", color & 0xFFFFFF);
        if(record) {
            positionHistory.add("#" + HexColor + " " + prevPosX + " " + prevPosY
                    + " " + posX + " " + posY);
        }
    }

    public void Rotate(Integer degrees){
        direction -= degrees;
    }

    public void SetRecordState(boolean record){
        this.record = record;
    } // Up eller Down

    public void SetColor(int color){
        this.color = color;
    }

    public void PrintRecording(){    // Iterar över positionHistory och printar.
        for (int i = 0; i < positionHistory.size(); ++i) {
            if (i != 0)
                System.out.print("\n");
            System.out.print(positionHistory.get(i));
        }
    }

}
