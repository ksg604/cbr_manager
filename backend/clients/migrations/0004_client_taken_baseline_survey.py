# Generated by Django 3.1.5 on 2021-04-04 09:05

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('clients', '0003_remove_client_risk_score'),
    ]

    operations = [
        migrations.AddField(
            model_name='client',
            name='taken_baseline_survey',
            field=models.BooleanField(default=False),
        ),
    ]
