INSERT INTO jpemailmarketing_course VALUES (1, 'course', 'mock@entando.com', 0, '0 0 0 * * *', 'Europe/Rome', '2014-01-01 10:10:10', '2014-01-01 10:10:10');
INSERT INTO jpemailmarketing_form (courseid, filecovername, fileincentivename, emailsubject, emailtext, emailbutton, emailmessagefriendly, createdat, updatedat) VALUES (1, '1.png', '1.txt', 'Here''s your reward', 'Hey, thanks for signing up! Click the link below to get your reward.

<a href="%recipient.jpemailmarketing_activatelink%">
%recipient.jpemailmarketing_rewardButtonText%
</a>

%recipient.jpemailmarketing_friendlyMessageText%', 'Redeem it now', 'Enjoy!', '2014-01-01 10:10:10', '2014-01-01 10:10:10');
INSERT INTO jpemailmarketing_coursemail VALUES (1, 1, 0, 1, 'Introductory content', '<p>Greet your subscriber reminding them who you are and why they are on this list. Then deliver the first bits of valuable information (don''t forget to replace these samples!).</p><p><a href="%recipient.jpemailmarketing_unsubscribelink%">unsubscribe</a></p>', '2014-05-14 14:30:00', '2014-05-14 14:30:00');
INSERT INTO jpemailmarketing_coursemail VALUES (2, 1, 1, 2, 'Educational content', '<p>Forget the marketing shenanigans and just focus on being as much useful as you can.</p><p><a href="%recipient.jpemailmarketing_unsubscribelink%">unsubscribe</a></p>', '2014-05-14 14:30:00', '2014-05-14 14:30:00');
INSERT INTO jpemailmarketing_coursemail VALUES (3, 1, 2, 2, 'Educational content + introduction to the product', '<p>Keep an overall educational tone, but you can give a quick introduction to the product. You are not selling anything, yet.</p><p><a href="%recipient.jpemailmarketing_unsubscribelink%">unsubscribe</a></p>', '2014-05-14 14:30:00', '2014-05-14 14:30:00');
INSERT INTO jpemailmarketing_coursemail VALUES (4, 1, 3, 4, 'Soft sell', '<p>Keep an overall educational tone, but also add a soft sell for your product. A casual mention at the end it''s fine most of the times.</p><p><a href="%recipient.jpemailmarketing_unsubscribelink%">unsubscribe</a></p>', '2014-05-14 14:30:00', '2014-05-14 14:30:00');
INSERT INTO jpemailmarketing_coursemail VALUES (5, 1, 4, 5, 'Educational content', '<p>Forget the marketing shenanigans and just focus on being as more useful as you can.</p><p><a href="%recipient.jpemailmarketing_unsubscribelink%">unsubscribe</a></p>', '2014-05-14 14:30:00', '2014-05-14 14:30:00');
INSERT INTO jpemailmarketing_coursemail VALUES (6, 1, 5, 6, 'Hard sell', '<p>Focus solely on your product, not on educational content. Remember to add a single call-to-action, a simple link is the best option in this context.</p><p><a href="%recipient.jpemailmarketing_unsubscribelink%">unsubscribe</a></p>', '2014-05-14 14:30:00', '2014-05-14 14:30:00');
INSERT INTO jpemailmarketing_coursemail VALUES (7, 1, 6, 4, 'Educational message', '<p>Forget the marketing shenanigans and just focus on being as more useful as you can.</p><p><a href="%recipient.jpemailmarketing_unsubscribelink%">unsubscribe</a></p>', '2014-05-14 14:30:00', '2014-05-14 14:30:00');

