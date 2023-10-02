from django.db import models

# Create your models here.
class GameResult(models.Model):
    game_result_id = models.AutoField(primary_key=True)
    user_id = models.CharField(max_length=45)
    rating = models.IntegerField(blank=True, null=True)
    asset = models.IntegerField(blank=True, null=True)
    land_amount = models.IntegerField(blank=True, null=True)
    stock_amount = models.IntegerField(blank=True, null=True)
    stock_asset = models.IntegerField(blank=True, null=True)
    loan_num = models.IntegerField(blank=True, null=True)
    door_used_num = models.IntegerField(blank=True, null=True)
    key_used_num = models.IntegerField(blank=True, null=True)
    angel_used_num = models.IntegerField(blank=True, null=True)
    survival_turn = models.IntegerField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'game_result'