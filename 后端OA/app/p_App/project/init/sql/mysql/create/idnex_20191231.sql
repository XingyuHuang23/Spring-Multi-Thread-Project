CREATE INDEX scan_result_pch ON scan_result(pch);
CREATE INDEX rule_result_pch ON rule_result(pch);
CREATE INDEX scan_result_title_id ON scan_result(scan_result_title_id);


CREATE INDEX scan_result_title_scan_type_id ON scan_result_title(scan_type_id);
CREATE INDEX scan_result_title_excute_pch ON scan_result_title(excute_pch);
CREATE INDEX dna_scan_type_text ON dna_scan_type(text);

CREATE INDEX scan_result_value ON scan_result(value);

CREATE INDEX scan_result_task_his_id ON scan_result(task_his_id);

CREATE INDEX scan_result_is_delete ON scan_result(is_delete);
CREATE INDEX scan_result_title_is_delete ON scan_result_title(is_delete);


CREATE INDEX scan_result_title_id ON external_result(scan_result_title_id);
CREATE INDEX pch ON external_result(pch);


