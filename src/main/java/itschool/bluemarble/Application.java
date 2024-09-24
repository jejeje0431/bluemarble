package itschool.bluemarble;

import itschool.bluemarble.dao.base.CommomDao;
import itschool.bluemarble.exception.needcheck.GameOver;
import itschool.bluemarble.model.factory.TileFactory;
import itschool.bluemarble.progress.GameByConsole;
import itschool.bluemarble.progress.Game;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        // System.out.println(TileFactory.getTiles());

        // 스캐너는 GameByConsole에서 만든 하나의 스캐너를 공유받음
        Scanner sc = new Scanner(System.in);

        // 플레이어 수
        int numberOfPlayer = 0;

        // 플레이어 수 입력받기
        while (true) {
            try {
                System.out.print("플레이어 수를 입력하세요 (2~4인) : ");
                numberOfPlayer = sc.nextInt();

                if (numberOfPlayer >= 2 && numberOfPlayer <= 4) {
                    // System.out.println("\n================================================================================================");
                    System.out.println(numberOfPlayer + "인 플레이 가능한 게임을 생성합니다.");
                    break; // 유효한 입력이므로 루프 종료
                } else {
                    System.out.println("2~4만 입력이 가능합니다.");
                }
            } catch (InputMismatchException e) {
                System.out.println("유효한 숫자를 입력해주세요.");
            } finally {
                sc.nextLine(); // 개행 버퍼 제거용
            }
        }

        // 게임 생성
        Game game = GameByConsole.createGameByConsole(numberOfPlayer);

        // 게임 시작
        try {
            game.start();
        } catch (GameOver e) {
            System.out.println(e.getMessage()); // "님이 게임을 우승하셨습니다. 게임을 종료합니다."
        }

    }
}
