CKEDITOR.editorConfig = function(config) {
	config.toolbar = [
		[
			'Format',
			'Link',
			'Unlink',
			'-',
			'Bold',
			'Italic',
			'Underline',
			'-',
			'NumberedList',
			'BulletedList',
			'-',
			'RemoveFormat',
			'-',
			'Undo',
			'Redo',
			'-',
			'Table',
			'-',
			'ShowBlocks',
			'-',
			'Maximize',
			'Preview',
			'Source'
		]
	];
	config.height = 250;
	config.forcePasteAsPlainText = true;
	config.docType = '<!DOCTYPE html>';
	config.pasteFromWordNumberedHeadingToList = true;
	config.pasteFromWordPromptCleanup = true;
};
