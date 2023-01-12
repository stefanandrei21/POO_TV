import DataBase.DataBase;
import DataBase.InputLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class de aici incep implementarea
 */
public class Main {

    public Main() { }
    public static void main(final String[] args) throws IOException {
        //creez un input loader
        InputLoader inputLoader = new InputLoader();
        //test manual
      // inputLoader.init("D:\\POO_TV\\checker\\resources\\in\\basic_4.json");
       inputLoader.init(args[0]);
        // retin elementele din input loader in baza mea de date
        DataBase input = inputLoader.readData();
        //instantiez clasa unde am logica principala a programului
        ProgramWorkflow work = new ProgramWorkflow();
        work.setDataBase(input);
        // creez lista de outputuri pe care urmeaza sa o printez in fisier
        List<Output> listToPrint = new ArrayList<Output>();
        listToPrint = work.work();
        //creez fisierul
        CreateJsonFile myJsonFile = new CreateJsonFile(listToPrint);
        myJsonFile.work();

    }
}
