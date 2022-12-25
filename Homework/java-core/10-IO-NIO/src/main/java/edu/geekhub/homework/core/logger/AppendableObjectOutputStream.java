package edu.geekhub.homework.core.logger;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class AppendableObjectOutputStream extends ObjectOutputStream {

    public AppendableObjectOutputStream(ObjectOutputStream oos) throws IOException {
        super(oos);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        reset();
    }
}
