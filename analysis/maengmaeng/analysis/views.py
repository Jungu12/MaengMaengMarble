from django.http import JsonResponse
from django.db.models import Case, When, Value, IntegerField, Avg, Count
from .models import GameResult

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
        'win_rate': win_rate
    }
    
    return JsonResponse(data)