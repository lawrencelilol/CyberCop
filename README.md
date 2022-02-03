# CyberCop

An App helps to analyze [Federal Trade Commission(FTC)](https://www.ftc.gov/about-ftc) cases.

## 
The Federal Trade Commission (FTC) is a government agency that is responsible for "protecting consumers and competition by preventing anticompetitive, deceptive, and unfair business practices through law enforcement, advocacy, and education without unduly burdening legitimate business activity." It conducts investigative cases against companies that engage in unlawful practices that harm consumers in any way. These cases are logged under [Cases and Proceedings](https://www.ftc.gov/enforcement/cases-proceedings) and are available for public review.

Table 1 shows a sample of cases FTC website. 

![image](https://user-images.githubusercontent.com/35508198/152411545-d9d3c4dc-776e-4bc9-9484-2e5843ab510a.png)

Important things to note in data:

1. Each case has a date, a title, a case type, and a case number. However, case type and case number may be missing for some cases.
2. The date is in yyyy-mm-dd format
3. Case type is given at the end of the Title within parentheses. The current data has Federal and Administrative as two case types
4. Case number is a string of some characters.

## GUI based application

The application can :
1. View all cases by inputed datafile
2. Search, Add, Modify, or Delete a case
3. Use data in TSV and CSV formats with additional columns
4. Make changes to case file and save it as a TSV file
5. Display a chart as shown Figure.5
6. Handle some runtime exceptions related to data

Figure 1 and Figure 2 show the opening screen and data-screen with names of important GUI controls of CyberCop.

![image](https://user-images.githubusercontent.com/35508198/152412123-9081a96f-bd1b-4fe7-abd0-a46c554e510b.png)

_Figure 1: CyberCop Opening Screen_


![image](https://user-images.githubusercontent.com/35508198/152412335-80e3864b-403d-45af-9281-c09f7e5c77fb.png)

_Figure 2: CyberCop with CyberCop.TSV opened_


![image](https://user-images.githubusercontent.com/35508198/152412470-6d0009f2-c79b-4384-8938-c404aa366778.png)

_Figure 3: Case View GUI controls to Add/Modify/Delete cases_

## Handling Error 

The app handles errors if the users does not input the data correctly or input wrong types of data

![image](https://user-images.githubusercontent.com/35508198/152415523-79d0da56-3b0a-4464-97d8-2640e39d2802.png)

The app also handles errors if the users does not input the data correctly or input wrong types of data.
It also handles runtime error related to the data.

_Figure 4: Exception handling_

## Data Visualization

The app can output a bar-chart with FTC cass counted for each year.

![image](https://user-images.githubusercontent.com/35508198/152415749-b1edddda-e4f2-4071-b5d0-eb09aa41b2bb.png)

_Figure 5: Bar-chart based on FTC case count_





