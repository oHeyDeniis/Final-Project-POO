
import java.util.Random;
import java.util.Scanner;

public class LancmentoaDados {
    private static final Random gerador = new Random();
    static Scanner sc = new Scanner(System.in);
    
        public static void main(String[] args) {
        int menu, opcao, pontuacao = 0;
        int result, qtdApost = 0;
        float aposta,  saldoBanca = 0;
        System.out.println("Vamos jogar?\n");
        
        do{
            System.out.print("JOGO DE LANÇAMENTO DE DADOS");
            menu = menuOpcoes();
            if(menu == 2){
                saldoBanca=recargaBanca(saldoBanca);
            }
            if(menu == 3){
                regrasJogo();
            }
            
            while(menu==1){
                System.out.println("\n\nDigite o valor da APOSTA. 'VAMOS VER SE VOCE TEM CORAGEM.'");
                System.out.print( "Valor da Aposta: R$ ");
                aposta=sc.nextFloat();
                sc.nextLine();
                saldoBanca = saldoBanca - aposta;
                tempo();
                if(saldoBanca < 0.5 ){
                    System.out.println("Sem saudo para aposta deseja recarregar:");
                    System.out.println("1- Fazer recarga.");
                    System.out.println("2- Sair");
                    opcao = sc.nextInt();
                    sc.nextLine();
                    //saldoBanca = recargaBanca(saldoBanca);
                    //sc.nextLine();
                    if(opcao == 1){
                        saldoBanca = recargaBanca(saldoBanca);
                    }else{
                        break;
                    }
                }else{
                    System.out.println("\nSerá que voce está com Sorte???");
                    result=somaDados();
                    qtdApost= qtdApost +1;
                    if(result==7 || result == 11){
                        tempo();
                        System.out.println("O Resultado é " + result);
                        System.out.println( "VOCE GANHOUUU!!! MEUS PARABENS DOBROU SUA APOSTA.");
                        aposta= aposta * 2;
                        saldoBanca = saldoBanca + aposta;
                        System.out.println( saldoBanca);
                    }
                    if(result == 2 || result == 3 || result == 12){
                        tempo();
                        System.out.println("O Resultado é " + result);
                        System.out.println( "VOCE PERDEU!!! MAIS SORTE DA PROXIMA VEZ....");
                        tempo();
                        System.out.println("SE TIVER CORAGEM DE CONTINUAR APOSTANDO.");
                        System.out.println( saldoBanca);
                    }
                    if(result == 4||result == 5||result == 6||result == 8||result == 9||result == 10){
                        
                        if (qtdApost ==1 ){
                            pontuacao = result;
                            System.out.println("O Resultado é " + result);
                            System.out.println("Tente outra vez.");
                            saldoBanca = saldoBanca + aposta;
                        }
                        if(qtdApost > 1 && pontuacao < 22){
                            pontuacao = pontuacao + result;
                            
                            System.out.println("O Resultado é " + result);
                            System.out.println( "Deu empate!!! MAIS SORTE DA PROXIMA VEZ....");
                            saldoBanca = saldoBanca + aposta;
                            System.out.println( "Saldo restante na Banca: R$ " + saldoBanca);
                        }
                    }
                }
            
            // menu = menuOpcoes();
            }
        }while(menu != 5);
        System.out.println("Fim do Jogo.");
    }

    public static int menuOpcoes(){
        int opcao;
        System.out.println("\n\n 1- LANÇAR DADOS.");
            System.out.println(" 2- ADICIONAR CREDITOS NA BANCA.");
            System.out.println(" 3- REGRAS DO JOGO.");
            System.out.println(" 4- VERFICAR HISTORICO.");
            System.out.println(" 5- SAIR\n");
            opcao=sc.nextInt();
            sc.nextLine();
        return opcao;
    }
    public static float recargaBanca(float banca){
        float recarga;
        System.out.println("Quantos R$ deseja colocar na banca? ");
                recarga=sc.nextFloat();
                sc.nextLine();
                tempo();
                banca= banca + recarga;
                System.out.println("Voce Adicionou R$ "+ recarga + "  a sua Banca.");
        return recarga;
    }
    public static void tempo(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Erro! ");
        }
    }
    public static int lancarDado1(){
        return gerador.nextInt(6)+1;
    }
    public static int lancarDado2(){
        return gerador.nextInt(6)+1;
    }
    public static int somaDados(){
        int resultado;
        resultado=lancarDado1() + lancarDado2();
        return resultado;
    }
    public static void regrasJogo(){
        System.out.println("Você lança dois dados. Cada dado tem seis faces que contêm um, dois, três, quatro,\r\n" + //
                        "cinco e seis pontos, respectivamente. Depois que os dados param de rolar, a soma\r\n" + //
                        "dos pontos nas faces viradas para cima é calculada.\r\n" + //
                        "- Se a soma for 7 (SEVEN) ou 11 (YO LEVEN) no primeiro lance, você ganha.\r\n" + //
                        "- Se a soma for 2 (SNAKE EYES), 3 (TREY) ou 12 (BOX CARS) no primeiro lance\r\n" + //
                        "(chamado “craps”), você perde (isto é, a “casa” ganha).\r\n" + //
                        "- Se a soma for 4, 5, 6, 8, 9 ou 10 no primeiro lance, essa soma torna-se sua\r\n" + //
                        "“pontuação”.\r\n" + //
                        "Para ganhar, você deve continuar a rolar os dados até “fazer sua pontuação” (isto é,\r\n" + //
                        "obter um valor igual à sua pontuação). Você perde se obtiver um 7 antes de fazer\r\n" + //
                        "sua pontuação");
        
    }
}
