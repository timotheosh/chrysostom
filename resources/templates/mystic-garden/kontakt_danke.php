<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//DE" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Mystic Garden - Kontakt Danke</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="JavaScript1.2" src="javascript.js" type="text/javascript"></script>
<link href="styles.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="container">
  <div id="kopf"> </div>
  <div id="menue"> <a href="#" class="mp1">&raquo; Home</a> <a href="uebermich.php" class="mp">&raquo; &Uuml;ber mich</a> <a href="bildergalerie.php" class="mp">&raquo; Bildergalerie</a> <a href="gaestebuch.php" class="mp">&raquo; G&auml;stebuch</a> <a href="links.php" class="mp">&raquo; Links</a> <a href="kontakt.php" class="mp">&raquo; Kontakt</a> <a href="impressum.php" class="mp">&raquo; Impressum</a> </div>
  <div id="contentkopf"> </div>
  <div id="content">
    <div id="sidebar">
      <h2>&raquo; Aktuelles</h2>
      <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.</p>
      <p>Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.</p>
      <h2>&raquo; Partnerseiten</h2>
      <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.</p>
      <p align="center"><a href="http://validator.w3.org/check?uri=referer"><img border="0" src="http://www.w3.org/Icons/valid-html401" alt="Valid HTML 4.01!" height="31" width="88"></a> <a href="http://jigsaw.w3.org/css-validator/check/referer"><img style="border:0;width:88px;height:31px" src="http://jigsaw.w3.org/css-validator/images/vcss" alt="Valid CSS!" /></a></p>
    </div>
    <div id="inhalt">
      <h1>&raquo; Kontaktbestätigung</h1>
      <?
          $weiter=0;
          setlocale(LC_TIME,"de_DE");
          // --- Daten für die e-Mail zusammenstellen ---
          $mail_text="Kontaktanfrage meinehomepage.de vom ".strftime("%c")."\n\n";
          if ($HTTP_POST_VARS[Anrede]) {
            $mail_text.="Name : $HTTP_POST_VARS[Anrede] $HTTP_POST_VARS[Name]\n";
            $weiter++;
          }
          if ($HTTP_POST_VARS[Telefon]) {
            $mail_text.="Telefon : $HTTP_POST_VARS[Telefon]\n";
            $weiter++;
          }
          if ($HTTP_POST_VARS[eMail]) {
            $mail_text.="e-Mail: $HTTP_POST_VARS[eMail]\n\n";
            $weiter++;
          }
          if ($HTTP_POST_VARS[Anfrage]) {
            $mail_text.="Anfrage :\n$HTTP_POST_VARS[Anfrage]\n";
            $weiter++;
          }
          $mail_text.="\n\nDiese eMail wurde über das Kontaktformular unter\nwww.meinehomepage.de erfasst und automatisch versandt\n\n";
  
          if ($weiter>=3) {
            $DieAdresse="info@meinehomepage.de";
            if ($HTTP_POST_VARS[eMail]<>"") {
              $DieAdresse=$HTTP_POST_VARS[eMail];
            }
            $header="From: \"$HTTP_POST_VARS[Anrede] $HTTP_POST_VARS[Name]\" <$DieAdresse>\n";
            mail("info@hpvorlagen24.de","Kontaktanfrage meinehomepage.de",$mail_text,$header);
            echo '<p>Vielen Dank,</p>
                  <p>f&uuml;r Ihr Interesse an meinem Leistungsangebot.</p>
                  <p>Ich werde Ihre Anfrage so schnell wie m&ouml;glich bearbeiten.</p>
                  <p><strong>Mit freundlichem Gruss<br>
                    Ihr Homepage Betreiber</strong></p>
                  <p>mail: <a href="mailto:info@meineemail.de">info@meineemail.de</a><br>
                     net: <a href="http://www.meinehomepage.de">www.meinehomepage.de</a></p>';
          } else {
            echo '<p>Ihre Kontaktanfrage konnte nicht bearbeitet werden da Sie nicht alle Pflichtfelder ausgefüllt haben.</p>
                  <p><strong>Mit freundlichem Gruss<br>
                    Ihr Homepage Betreiber</strong></p>
                  <p>mail: <a href="mailto:info@meineemail.de">info@meineemail.de</a><br>
                     net: <a href="http://www.meinehomepage.de">www.meinehomepage.de</a></p>';
          }
  
        ?>
    </div>
  </div>
  <div id="fuss"> Design by <a href="http://www.hpvorlagen24.de">www.hpvorlagen24.de</a> copyright &copy; 2005 </div>
</div>
</body>
</html>
