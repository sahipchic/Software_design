import java.io.OutputStreamWriter;
import java.util.Scanner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import model.MasterRequest;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ActorSystem system = ActorSystem.create("system");

        String in;
        while ((in = scanner.nextLine()) != null) {
            if (in.equals("/q")) {
                System.exit(0);
            }
            OutputStreamWriter outputWriter = new OutputStreamWriter(System.out);
            ActorRef master = system.actorOf(Props.create(MasterSearchActor.class, outputWriter, new ResultHolder()), "master");
            master.tell(new MasterRequest(in, 0), ActorRef.noSender());
        }
    }
}
