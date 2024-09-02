package br.ufal.ic.p2.myfood;
import easyaccept.EasyAccept;
 
 
public class Main {
    public static void main(String[] args) {
         
        String[] args2 = {"br.ufal.ic.p2.myfood.Facade",
                "tests/us1_1.txt", "tests/us1_2.txt",
                "tests/us2_1.txt", "tests/us2_2.txt",        //2_1 para 2_2 o arquivo tem de ser o mesmo criado na 2_1, por algum motivo o zerar sistema ta de mal cmg nesse caso
                "tests/us3_1.txt", "tests/us3_2.txt",
                "tests/us4_1.txt", "tests/us4_2.txt",
        };
        EasyAccept.main(args2);
    }

}
