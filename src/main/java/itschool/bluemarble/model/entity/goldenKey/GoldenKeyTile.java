//담당자: 이승종, 이승환

//황금 열쇠 추상화
//황금 열쇠 이동
//draw로 뽑은 카드 제거
//suffle로 카드 섞는 기능 추가
//카드가 전부 떨어졌을 때 다시 배치 및 셔플
//프리 패스 키, 수입 기능 추가

package itschool.bluemarble.model.entity.goldenKey;

import itschool.bluemarble.exception.violation.PlayerHasNoLandViolation;
import itschool.bluemarble.model.entity.Player;
import itschool.bluemarble.model.entity.goldenKey.ifs.MoveFunction;
import itschool.bluemarble.model.entity.tile.Tile;
import itschool.bluemarble.model.entity.goldenKey.ifs.InstantFunction;
import itschool.bluemarble.progress.GameByConsole;

import java.util.*;

public class GoldenKeyTile extends Tile {


    public GoldenKeyTile(int index) {
        super(index,"황금열쇠");
    }

    private static ArrayList<GoldenKey> goldenKeyList = new ArrayList<>();

    // 초기화 메서드
    private static void initializeGoldenKeys() {
        goldenKeyList.addAll(Arrays.asList(
                // 이동 시작 (9종)
                new GoldenKey("고속도로", "출발지로 이동합니다.",(MoveFunction) ((player) -> player.moveByAbsoluteValue(0))),
                new GoldenKey("관광여행", "부산으로 이동합니다.",(MoveFunction) ((player) -> player.moveByAbsoluteValue(25))),
                new GoldenKey("관광여행", "서울로 이동합니다.",(MoveFunction) ((player) -> player.moveByAbsoluteValue(39))),
                new GoldenKey("관광여행", "제주도로 이동합니다.",(MoveFunction) ((player) -> player.moveByAbsoluteValue(5))),
                new GoldenKey("무인도", "무인도로 이동합니다.",(MoveFunction) ((player) -> player.moveByAbsoluteValue(10))),
                new GoldenKey("사회복지기금 배당", "사회복지기금으로 이동합니다.",(MoveFunction) ((player) -> player.moveByAbsoluteValue(20))),
                // new GoldenKey("세계일주 초대권", "현재 위치에서 현재 위치로 한 바퀴 돌아오며, 월급 및 사회복지기금을 받을 수 있다.", ),
                new GoldenKey("우주여행 초청장", "우주여행으로 이동합니다.",(MoveFunction) ((player) -> player.moveByAbsoluteValue(30))),
                // new GoldenKey("유람선 여행", "우주여행 칸으로 간다. (황금열쇠로 이동하는 경우 소유자가 있어도 무료 이용 가능하다.)",),
                new GoldenKey("이사", "뒤로 두 칸 이동합니다.",(MoveFunction) ((player) -> player.moveByRelativeValue(-2))),
                new GoldenKey("이사","뒤로 세 칸 이동합니다." ,(MoveFunction) ((player) -> player.moveByRelativeValue(-3))),
                new GoldenKey("특정 장소로 이동","원하는 곳으로 이동 가능하다. (이동은 한 방향으로 고정되어 있기에 이동하는 길에 출발지가 있으면 월급을 받는다.)", (MoveFunction) (player) -> player.moveByAbsoluteValue(GameByConsole.requestTileIndex(player))),
                // new GoldenKey("항공여행","콩코드 여객기를 타고 타이베이로 이동(주인이 있을 경우 통행료 지불 여객기, 건물 주인)" ,),
                // 이동 끝

                // 수입 시작 (6종)
                new GoldenKey("경주 우승", "10만원을 받습니다.",(InstantFunction) ((player) -> player.plusAmount(100000))),
                new GoldenKey("노벨평화상 수상", "30만원을 받습니다.",(InstantFunction) ((player) -> player.plusAmount(300000))),
                new GoldenKey("복권 당첨", "20만원을 받습니다.",(InstantFunction) ((player) -> player.plusAmount(200000))),
                // new GoldenKey("생일 축하", "전원에게 축하금 1만원씩 받기",),
                new GoldenKey("연금 혜택", "5만원을 받습니다.",(InstantFunction) ((player) -> player.plusAmount(50000))),
                new GoldenKey("장학금 혜택", "10만원을 받습니다.",(InstantFunction) ((player) -> player.plusAmount(100000))),
                //수입 끝

                // 지출 시작 (8종)
                // new GoldenKey("건물 유지비 지불", "지금까지 지은 건물의 개수에 따라 은행에 돈을 낸다. (건물별로 금액을 지정할 필요가 있다.)",),
                // new GoldenKey("건물 수리비 지불", "가지고 있는 건물 수만큼 은행에 돈을 낸다. 호텔: 개당 10만원, 빌딩: 개당 6만원, 별장: 개당 3만원",),
                new GoldenKey("과속 운전", "5만원을 은행에 지불",(InstantFunction) ((player) ->  player.minusAmount(50000))),
                // new GoldenKey("방범비", "가지고 있는 건물 수만큼 은행에 돈을 낸다. 호텔: 개당 5만원, 빌딩: 개당 3만원, 별장: 개당 1만원",),
                new GoldenKey("병원비", "병원비로 5만원을 은행에 냅니다.",(InstantFunction) ((player) ->  player.minusAmount(50000))),
                // new GoldenKey("정기종합소득세", "가지고 있는 건물 수만큼 은행에 돈을 낸다. 호텔: 개당 15만원, 빌딩: 개당 10만원, 별장: 개당 3만원",),
                new GoldenKey("해외 유학", "해외 유학비로 10만원을 은행에 냅니다.",(InstantFunction) ((player) ->  player.minusAmount(100000))),
                new GoldenKey("반액대매출", "해당 턴에 구매한 건물 및 가장 비싼 땅을 반값으로 은행에 팝니다.",(InstantFunction) (Player::sellAtHalfPrice)),
                // 지출 끝

                // 보유가능한 황금열쇠 (2종)
                new TollFreePassKey("우대권", "이 카드를 소모하여 1회 통행료 지불 면제, 우대권 플레이어 간 경매 가능"), // 우대권
                new EscapeIslandKey("무인도 탈출권", "무인도에 갇혔을 때 이 카드를 사용하면 더블이 나오지 않아도 무인도를 탈출할 수 있다.(20만원에 은행 매각 가능)")
        ));

        shuffle();
    }

    public GoldenKey draw() {
        if (goldenKeyList.size() == 0){
            initializeGoldenKeys();
            shuffle();
        }
        return goldenKeyList.remove(0);
    }

    private static void shuffle(){
        Collections.shuffle(goldenKeyList);

    }
}