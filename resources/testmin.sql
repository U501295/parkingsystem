use prod;
select min(PARKING_NUMBER)
from parking
where AVAILABLE = true
  and TYPE = CAR;