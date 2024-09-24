// 담당자 : 오재헌
// 부루마블 판이 나오고 주사위 결과 나오도록 수정

package itschool.bluemarble.progress;

import itschool.bluemarble.exception.event.HoldableKeyEvent;
import itschool.bluemarble.exception.event.LoanEvent;
import itschool.bluemarble.exception.violation.BankruptPlayerViolation;
import itschool.bluemarble.exception.needcheck.GameOver;
import itschool.bluemarble.exception.violation.PlayerHasNoLandViolation;
import itschool.bluemarble.exception.violation.PlayerHasNoMoneyViolation;
import itschool.bluemarble.model.entity.Dice;
import itschool.bluemarble.model.entity.Player;
import itschool.bluemarble.model.entity.goldenKey.GoldenKeyTile;
import itschool.bluemarble.model.entity.goldenKey.ifs.MoveFunction;
import itschool.bluemarble.model.entity.tile.*;
import itschool.bluemarble.model.entity.tile.abs.City;
import itschool.bluemarble.model.entity.tile.abs.PurchasableTile;
import itschool.bluemarble.model.factory.TileFactory;
import itschool.bluemarble.model.entity.goldenKey.GoldenKey;
import itschool.bluemarble.model.entity.goldenKey.ifs.InstantFunction;
import itschool.bluemarble.progress.ifs.GameInterface;
import itschool.bluemarble.progress.phase.MovePhase;
import itschool.bluemarble.progress.phase.RollDicePhase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 게임 룰을 담당하는 클래스
// 게임 안에는 매턴이 발생하며 (플레이어가) 매턴마다 순서에 따라 주사위를 굴린다.
public abstract class Game implements GameInterface {
    final protected int NUMBER_OF_PLAYER;
    final protected List<Tile> TILES = TileFactory.getTiles(); // Tiles에 getTiles 메소드 필요
    final protected List<Player> PLAYERS = new ArrayList<Player>();
    protected int turn = 1;

    protected Game(int numberOfPlayer) {
        this.NUMBER_OF_PLAYER = numberOfPlayer;
    }

    public static void ClearConsole() {
        try {
            // 콘솔 화면을 지우는 명령어 실행
            if (System.getProperty("os.name").startsWith("Windows")) {
                // cmd를 사용하여 콘솔 지우기
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                // PowerShell을 사용하여 콘솔 지우기
                // new ProcessBuilder("powershell", "-Command", "Clear-Host").inheritIO().start().waitFor();
            } else {
                // Unix/Linux/Mac에서 ANSI 이스케이프 코드 사용
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
            System.out.println();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() throws GameOver {
        while (true) {
            for (Player player : PLAYERS) {
                try { // Exception은 어느서 발생시킬 건지 담당자에게 나눠줘야 함, 게임 내 발생하는 예외는 Game에 어떻게 처리할 지 정한다.
                    proceedPhase(player);

                } catch (PlayerHasNoLandViolation e1) { // 황금열쇠:반액대매출, 반액대매출을 하고 싶어도 땅이 없음
                    printOutOfException(e1); // 반액대매출, "님이 땅을 가지고 있지 않습니다."
                } catch (HoldableKeyEvent e1) { // 우대권, 무인도 탈출권
                    printOutOfException(e1); // 님이/가 황금열쇠"+ goldenKeyTitle + "을 사용하셨습니다."
                } catch (PlayerHasNoMoneyViolation e1) { // 지출을 해결할 현금 부족
                    try {
                        printOutOfException(e1); // "이/가 보유한 돈이 부족합니다."

                        if(true/*대출 수행 가능 여부를 확인*/) { // 대출 가능
                            throw new LoanEvent(player.getName());
                        } else { // 대출 불가
                            // 현재 보유한 부동산 매각(플레이어가 가격이 높은 순으로 판매하고 빚을 갚을 수 있다면 그렇게 처리.


                            // 부동산을 매각하여 갚기 로직 작성
                            
                            
                            // 매각으로도 해결이 안된다면 다시 PlayerHasNoMoneyViolation 다시 발생 -> 파산 처리 할거임
                            throw new PlayerHasNoMoneyViolation(player.getName());
                        }
                    } catch (LoanEvent e2) { // 대출4
                        // 대출 수행하고 갚기
                        printOutOfException(e2); // "님, 대출은 진행하시겠습니까?"
                        // 대출 수행 페이즈 로직 필요 (player의 대출 절차 메소드 수행, 해당 플레이어가 은행에 돈을 빌린다음 몇 턴 후에 갚는다)
                        // 상대 플레이어에게 갚는 로직 필요
                    } catch (PlayerHasNoMoneyViolation e2) { // BankruptPlayerViolation bankruptcy
                        // 대출조차 못하면 파산 처리한다
                        throw new BankruptPlayerViolation(player.getName());
                    }
                } catch (BankruptPlayerViolation e1) {
                    // 파산 처리
                    printOutOfException(e1); // "님이 파산하셨습니다."
                    PLAYERS.remove(player);
                }

                if (PLAYERS.size() == 1) throw new GameOver(PLAYERS.get(0).getName());
                // 라운드 개념을 추가하려면 추가 또는 수정 바람
            }
            turn++; // 한번씩 돌아간 후에 턴을 증가
            if (PLAYERS.size() <= 1 || turn > 100){ // 100턴이 끝나면 게임이 종료
                throw new GameOver(winplayer());
            }
        }
        // PLAYERS.get(0).setWinCount(PLAYERS.get(0).getWinCount()+1); // 승리 횟수 저장은 제거
    }

    private String winplayer() {
        Player winner = PLAYERS.get(0);
        for (Player player : PLAYERS) {
            if (player.myAllAsset() > winner.myAllAsset()) {
                winner = player;
            }
        }
        return winner.getName();
    }

    private void proceedPhase(Player player) throws RuntimeException {
        Dice dice = new Dice();

        // 타일 맵 출력
        ClearConsole();
        showMap();

        // 현재 플레이어 정보 출력
        printOutOfPlayerInfo(player);

        do { // 더블이 나오면 나머지 페이즈 다 수행하고 다시 던진다.
            if (checkTurn(player, dice)) { // 턴 수행 여부 및 직전에 더블인지 체크

                dice = RollDicePhase.rollDice(player, dice);

                // 주사위 결과 출력
                printOutDiceResult(player, dice);

                int beforeRollIndex = player.getLocation();
                int afterRollIndex = -1;

                if (dice.getDoubleCount() == 3 & dice.isDouble()) {
                    player.moveByAbsoluteValue(10); // 무인도(10) 타일로 이동
                    // afterRollIndex = 10;
                    break;
                } else {
                    if (player.checkIsland()) {
                        player.stayIsland(dice.isDouble());
                        break;
                    }else {
                        afterRollIndex = MovePhase.move(player, (dice.getDice1() + dice.getDice2()));
                    }
                }

                // 이동 결과 출력
                printOutOfMoving(player);

                // 도착한 타일에 대한 수행 페이즈(황금열쇠, 도시, 특수 도시)
                arriveTilePhase(player, afterRollIndex);

                // 현재 플레이어 정보 출력
                printOutOfPlayerInfo(player);
                if (player.checkIsland()) {
                    break;
                }

            }
        } while (dice.isDouble() ); // 더블이면 턴 내 페이즈를 재수행한다.
    }

    private boolean checkTurn(Player player, Dice dice) {
        StringBuilder message = new StringBuilder();

        message.append("\n============================  " + turn + "번째 턴입니다. " + player.getName() + "님 차례" + "  ============================\n\n");
        message.append(player.getName());

        if(dice.getDoubleCount() > 0)
            message.append("님, 더블이 나왔습니다. 주사위를 다시 굴려주세요.");
        else
            message.append("님, 주사위를 굴려주세요.");

        return confirm(message.toString());
    }

    // 도착한 타일 구입 확인, 구입, 통행료 지불 등을 할 추가 페이즈
    private void arriveTilePhase(Player player, int index) throws RuntimeException {

        Tile currentTile = (Tile) TILES.get(index);

        // 도착한 타일이 무엇인지 확인
        if (currentTile instanceof GoldenKeyTile) { // 황금열쇠 타일
            arriveGoldenKeyTile(player, currentTile);
        }

        if (currentTile instanceof PurchasableTile) { // 건물 지을 수 있는 도시
            arrivePurchasableTile(player, (PurchasableTile)currentTile);
        } else if (currentTile instanceof Island) { // 무인도
             arriveIsland(player, (Island) currentTile);
        } else if (currentTile instanceof GiveDonation) { // 사회복지기금 지급처
            arriveGiveDonation(player, currentTile);
        } else if (currentTile instanceof DonationParty) { // 사회복지기금 수령처
            arriveDonationParty(player, currentTile);
        } else if (currentTile instanceof SpaceTravel) { // 우주 여행
            // 우주 여행 등 특수 타일에 대한 특수
            arriveSpaceTravel(player);
        } else {
            // 시작 타일.. 수행 할 것  없음
        }
    }

    private void arriveGoldenKeyTile(Player player, Tile tile) throws PlayerHasNoLandViolation, PlayerHasNoMoneyViolation {
        GoldenKey goldenKey = null;

        goldenKey = player.drawGoldenKey((GoldenKeyTile) tile);

        printOutOfDrawedGoldenKey(player, goldenKey);

        if (goldenKey.getFunction() instanceof InstantFunction) { // 즉시 수-행해야 하는 인스턴트 평션
            ((InstantFunction) goldenKey.getFunction()).execute(player); // 반액대매출에서 PlayerHasNoLandViolation 예외 던질 수 있음
            if (goldenKey.getFunction() instanceof MoveFunction)
                arriveTilePhase(player, player.getLocation());
        }
    }

    private void arrivePurchasableTile(Player player, PurchasableTile tile) throws PlayerHasNoMoneyViolation {
        boolean shouldPay = tile.shouldPay(player);
        if (shouldPay) { // 통행료를 내야하는 상황
            System.out.println("============================================================================================\n");
            System.out.println(tile.getOwner().getName() + "님 땅에 도착했습니다ㅠㅠ 통행료 " + GameByConsole.formatWithCommas(tile.getToll()) + "원을 지불합니다.");
            System.out.println("\n============================================================================================");
            // confirmToUseGoldenKey 발생 가능 예외
            // RuntimeException : 해당 황금열쇠가 존재하지 않음
            // HoldableKeyEvent : 황금열쇠 사용 완료
            try {
                if(player.hasTollFreePassKey(player))
                    confirmToUseGoldenKey(player, player.findTollFreePassKey());
            } catch (HoldableKeyEvent e) {
                System.out.println(e.getMessage());
                return;
            }

            // 땅의 주인에게 통행료 지불 로직 필요
            // 통행료 지불 불가한 경우 PlayerHasNoMoneyViolation 발생
            player.payAmountTo(tile.getOwner(), tile.getToll());

        } else {
            if(tile.getOwner() == null) // 부동산 주인이 없는 경우 구입 여부 확인 및 구입 진행
                confirmToBuyPurchasableTile(player, tile);
            else // 내가 땅 주인인 경우
                if(tile instanceof City)
                    confirmToBuyConstruction(player, (City)tile);
        }
    }

    private void arriveIsland(Player player, Island island) {
        island.getSpecialFunction();
        player.arriveIsland(); // 카운트가 0이 되면 탈출하도록 초기 카운트 3으로 설정
    }

    private void arriveFixedTollCity(Player player, Tile currentTile) {
        // 가격 고정인 도시 도착 로직 필요
    }

    private void arriveGiveDonation(Player player, Tile currentTile) {
        Tile giveDonation = TILES.get(20);
        DonationParty donationParty = DonationParty.getInstance();

        // 은행에 15만원 지불
        player.payAmountToBank(150_000);

        // 사회복지기금 수령처 잔액은 15만원 증가
        donationParty.plusAmount(150_000);
    }

    private void arriveDonationParty(Player player, Tile currentTile) {
        DonationParty donationParty = DonationParty.getInstance();
        // 도착한 플레이어에게 지불
        donationParty.payAmountTo(player, donationParty.getAmount());
    }

    private void arriveSpaceTravel(Player player) {
        // 인덱스 32번(컬럼비아 호) 타일 정보
        PurchasableTile columbia = (PurchasableTile) TILES.get(32);
        SpaceTravel spaceTravel = new SpaceTravel();

        // 컬럼비아 호 없으면 은행에 20만원 지불, 있다면 땅 주인에게 20만원 지불
        spaceTravel.payFee(player, columbia.getOwner());
        choiceTile(player);
    }
}
