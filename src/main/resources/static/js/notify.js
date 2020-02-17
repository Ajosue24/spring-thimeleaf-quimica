function init_PNotify(title, type, message) {

	if (typeof (PNotify) === 'undefined') {
		return;
	}

	new PNotify(
			{
				title : title,
				type : type,
				text : message,
				styling : 'bootstrap3',
			});

};