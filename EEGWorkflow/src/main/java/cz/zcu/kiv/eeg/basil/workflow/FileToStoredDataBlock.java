package cz.zcu.kiv.eeg.basil.workflow;

import cz.zcu.kiv.WorkflowDesigner.Annotations.*;
import org.apache.commons.io.FileUtils;

import java.io.*;

import static cz.zcu.kiv.WorkflowDesigner.Type.FILE;
import static cz.zcu.kiv.WorkflowDesigner.Type.STREAM;

@BlockType(type="FileToStoredData", family = "File" )
public class FileToStoredDataBlock {

    @BlockProperty(name = "StoredFile",  type = FILE)
    private File file;

    @BlockOutput(name = "PipeOut",  type = STREAM)
    private PipedOutputStream pipedOut = new PipedOutputStream();

    @BlockExecute()
    public void process() throws Exception {

        FileInputStream fileInStream = new FileInputStream(file);

//        int b;
//        while((b = fileInStream.read())!= -1) {
//            pipedOut.write(b);
//            pipedOut.flush();
//        }
        byte[] bytes= new byte[1];
        while(fileInStream.read(bytes)!=-1){

            pipedOut.write(bytes);
            pipedOut.flush();
        }

        fileInStream.close();
        pipedOut.close();

    }


}
