from django.http import JsonResponse
from django.db.models import Case, When, Value, IntegerField, Avg, Count, Max
from .models import GameResult

# 평균 등수 백분율 계산 함수
def calculate_rank_percentage(rating, min_rating, max_rating):
    if max_rating <= rating <= min_rating:
        percentage = ((rating - max_rating) / (min_rating - max_rating)) * 100
        return percentage
    else:
        return None

# 백분율 계산 함수
def calculate_percentage(value, min_value, max_value):
    if min_value <= value <= max_value:
        percentage = (value - min_value) / (max_value - min_value) * 100
        return percentage
    else:
        return 0  # 범위 밖의 값에 대해서는 None을 반환하거나 처리 방식을 변경할 수 있습니다.

def playstyle(rating_average, asset_average, land_count, stock_average, loan_count, door_count, key_count, angel_count, turn_count):
    max_asset = GameResult.objects.aggregate(max_asset=Max('asset'))['max_asset']
    max_land = GameResult.objects.aggregate(max_land=Max('land_amount'))['max_land']
    max_loan = GameResult.objects.aggregate(max_loan=Max('loan_amount'))['max_loan']
    max_door = GameResult.objects.aggregate(max_door=Max('door_amount'))['max_door']
    
    # 평균 등수에 대한 백분율
    per_rating = calculate_rank_percentage(rating_average, 1.5, 1)
    # 보유 자산에 대한 백분율
    per_asset = calculate_percentage(asset_average, 250000000, max_asset)
    # 보유 땅에 대한 백분율
    per_land = calculate_percentage(land_count, 6, max_land)
    # 보유 주삭 자산에 대한 백분율
    per_stock = calculate_percentage(stock_average, rating_average*0.3, rating_average)
    # 생존 턴수에 대한 백분율
    per_turn = calculate_percentage(turn_count, 25, 30)
    # 대출 횟수에 대한 백분율
    per_loan = calculate_percentage(loan_count, 1, max_loan)
    # 어디로든문 횟수에 대한 백분율
    per_door = calculate_percentage(door_count, 4, max_door)
    # 천사카드 사용 횟수에 대한 백분율
    per_angel = calculate_percentage(angel_count, 1, 4)
    # 황금 열쇠 이용 횟수에 대한 백분율
    per_key = calculate_percentage(key_count, 6, 10)
    
    style = max(per_rating,per_angel,per_land,per_asset,per_stock,per_turn,per_land,per_loan,per_door,per_key)
    if style == 0:
        return
    if style == per_rating:
        return "리오넬 메시"
    elif style == per_asset:
        return "베르나르 아르노"
    elif style == per_land:
        return "도널드 브렌"
    elif style == per_stock:
        return "워렌 버핏"
    elif style == per_turn:
        return "베어 그릴스"
    elif style == per_loan:
        return "이상민"
    elif style == per_door:
        return "빠니보틀"
    elif style == per_angel:
        return "에드윈 카스트로"
    elif style == per_key:
        return "루카스 호칸손"
    

def my_analysis_view(request, user_id):
    # 유저의 승리 횟수와 패배 횟수를 계산
    win_loss_counts = (
        GameResult.objects
        .filter(user_id=user_id)
        .aggregate(
            win_count=Count(
                Case(When(rating=1, then=Value(1)), output_field=IntegerField())
            ),
            loss_count=Count(
                Case(When(rating__gt=1, then=Value(1)), output_field=IntegerField())
            )
        )
    )
    win_count = win_loss_counts['win_count']
    loss_count = win_loss_counts['loss_count']

    
    # 게임 수
    game_count = GameResult.objects.filter(user_id=user_id).count()
    # 평균 등수
    rating_average = GameResult.objects.filter(user_id=user_id).aggregate(avg_rating=Avg('rating'))['avg_rating']
    # 총 자산
    asset_average = '{:,.2f}'.format(GameResult.objects.filter(user_id=user_id).aggregate(avg_asset=Avg('asset'))['avg_asset'])
    # 각 순위별 횟수
    rating = [list(GameResult.objects.filter(user_id=user_id).values_list('rating', flat=True)).count(i) for i in range(1, 5)]
    # 땅 개수
    land_count = GameResult.objects.filter(user_id=user_id).aggregate(avg_land_amount=Avg('land_amount'))['avg_land_amount']
    # 주식 수
    stock_count = GameResult.objects.filter(user_id=user_id).aggregate(avg_stock_amount=Avg('stock_amount'))['avg_stock_amount']
    # 주식 자산
    stock_average = '{:,.2f}'.format(GameResult.objects.filter(user_id=user_id).aggregate(avg_stock_asset=Avg('stock_asset'))['avg_stock_asset'])
    # 대출 횟수
    loan_count = GameResult.objects.filter(user_id=user_id).aggregate(avg_loan_num=Avg('loan_num'))['avg_loan_num']
    # 어디로든 문 이용 횟수
    door_count = GameResult.objects.filter(user_id=user_id).aggregate(avg_door_used_num=Avg('door_used_num'))['avg_door_used_num']
    # 황금열쇠 사용 횟수
    key_count = GameResult.objects.filter(user_id=user_id).aggregate(avg_key_used_num=Avg('key_used_num'))['avg_key_used_num']
    # 천사카드 사용 횟수
    angel_count = GameResult.objects.filter(user_id=user_id).aggregate(avg_angel_used_num=Avg('angel_used_num'))['avg_angel_used_num']
    # 생존 턴
    turn_count = GameResult.objects.filter(user_id=user_id).aggregate(avg_survival_turn=Avg('survival_turn'))['avg_survival_turn']
    # 승률 계산
    total_games = win_count + loss_count
    win_rate = (win_count / total_games) * 100 if total_games > 0 else 0
    
    # 플레이스타일
    my_playstyle = playstyle(rating_average, asset_average, land_count, stock_average, loan_count, door_count, key_count, angel_count, turn_count)
    
    # JSON 응답 반환
    data = {
        'game_count': game_count, 
        'rating_average': rating_average,
        'asset_average': asset_average,
        'land_count': land_count,
        'stock_count': stock_count,
        'stock_average': stock_average,
        'rating': rating,
        '1st' : rating[0],
        '2nd' : rating[1],
        '3th' : rating[2],
        '4th' : rating[3],
        'loan_count': loan_count,
        'door_count': door_count,
        'key_count': key_count,
        'angel_count': angel_count,
        'turn_count': turn_count,
        'win_count': win_count, 
        'loss_count': loss_count,
        'win_rate': win_rate,
        'playstyle' : my_playstyle
    }
    
    return JsonResponse(data)