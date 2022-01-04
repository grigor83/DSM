import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

class  AppendableOOS extends ObjectOutputStream
{    
    public AppendableOOS(OutputStream out) throws IOException {
        super(out);
    }
     
    @Override
    protected void writeStreamHeader() throws IOException {
        //super.writeStreamHeader();
        //reset();
    }  
}