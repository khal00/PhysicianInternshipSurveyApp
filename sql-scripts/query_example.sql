select avg(NULLIF(section_columns,0)) as sec_avg from (select tutor as section_columns from internship_section where name='Obsterics and Gynecology'
union all select unit from internship_section where name='Obsterics and Gynecology' and 
union all select ward from internship_section where name='Obsterics and Gynecology') quest_avg;