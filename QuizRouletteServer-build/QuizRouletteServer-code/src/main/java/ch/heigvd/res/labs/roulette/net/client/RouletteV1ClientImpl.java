package ch.heigvd.res.labs.roulette.net.client;


import ch.heigvd.res.labs.roulette.data.EmptyStoreException;
import ch.heigvd.res.labs.roulette.data.JsonObjectMapper;
import ch.heigvd.res.labs.roulette.data.Student;
import ch.heigvd.res.labs.roulette.net.protocol.InfoCommandResponse;
import ch.heigvd.res.labs.roulette.net.protocol.RandomCommandResponse;


import ch.heigvd.res.labs.roulette.net.protocol.RouletteV1Protocol;

import java.io.*;

import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class implements the client side of the protocol specification (version 1).
 *
 * @author Olivier Liechti
 */
public class RouletteV1ClientImpl implements IRouletteV1Client {

    protected static final Logger LOG = Logger.getLogger(RouletteV1ClientImpl.class.getName());

    protected Socket conn;

    protected BufferedWriter os;
    protected BufferedReader is;


    public RouletteV1ClientImpl() {
        conn = new Socket();
    }


    @Override
    public void connect(String server, int port) throws IOException {

        if (conn.isConnected()) disconnect();

        conn = new Socket(server, port);

        os = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        is = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        is.readLine();
    }

    @Override
    public void disconnect() throws IOException {


        send(RouletteV1Protocol.CMD_BYE);

        os.close();
        is.close();

        conn.close();
    }

    @Override
    public boolean isConnected() {
        return conn.isConnected() && !conn.isClosed();
    }

    @Override
    public void loadStudent(String fullname) throws IOException {

        if (fullname == null || fullname.isEmpty()) return;

        send(RouletteV1Protocol.CMD_LOAD);
        send(fullname);
        send(RouletteV1Protocol.CMD_LOAD_ENDOFDATA_MARKER);

        //flush answers
        is.readLine();
        is.readLine();
    }

    @Override
    public void loadStudents(List<Student> students) throws IOException {
        if (students == null || students.isEmpty())
            return;

        for (Student s : students) {
            loadStudent(s.getFullname());
        }
    }

    @Override
    public Student pickRandomStudent() throws EmptyStoreException, IOException {

        send(RouletteV1Protocol.CMD_RANDOM);

        RandomCommandResponse response = JsonObjectMapper.parseJson(is.readLine(), RandomCommandResponse.class);

        if (response.getError() != null) throw new EmptyStoreException();

        return new Student(response.getFullname());
    }

    @Override
    public int getNumberOfStudents() throws IOException {
        send(RouletteV1Protocol.CMD_INFO);

        return JsonObjectMapper.parseJson(is.readLine(), InfoCommandResponse.class).getNumberOfStudents();
    }

    @Override
    public String getProtocolVersion() throws IOException {

        send(RouletteV1Protocol.CMD_INFO);

        return JsonObjectMapper.parseJson(is.readLine(), InfoCommandResponse.class).getProtocolVersion();
    }

    protected void send(String msg) throws IOException {
        os.write(msg);
        os.newLine();
        os.flush();
    }

}
