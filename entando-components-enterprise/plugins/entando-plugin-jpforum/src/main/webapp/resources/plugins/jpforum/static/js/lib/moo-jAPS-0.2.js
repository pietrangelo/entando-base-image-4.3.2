/*
 Class: HoofEd
 Genera un piccolo editor da utilizzare per la formattazione del testo nelle textarea
 
 Parameters:
 buttonsFileName - stringa, suffisso con il quale indicare i file catalogo e delle lingue
 basePath - stringa, URI della cartella contentende i file di catalogo e della lingua
 textareaID - stringa, l'id (html) della textarea di riferimento
 toolClass - stringa, classe css da applicare all'HoofEd
 toolPosition - stringa, indica dove posizionare Hoofed rispetto <textareaID>. Accetta i valori: *before* ed *after* (default: before)
 buttons - array, array js dove dichiarare i bottoni (i bottoni devono essere presenti nel file di catalogo)
 lang - stringa, di 2 caratteri rappresenta la lingua dell'editor
 separatorClass - string, css class for the separator element
 separatorElement - string, element type for the separator
 separatorText - string, text separator
 
 Per il funzionamento di HoofEd si pressupone che esistano due file: il file con il catalogo dei bottoni disponibili e il file contenente le etichette tradotte.
 
 Il file di catalogo si deve chiamare *<buttonsFileName>*.js
 
 Il file con delle traduzioni si deve chiamare *<buttonsFileName>_<lang>*.js
 File di supporto:
 Esempio di file catalogo *<buttonsFileName>*.js
 (start code)
 
 var HoofEd_buttons = { //il nome dell'oggetto DEVE rimanere inviariato
 "bold" : ['<span class="bold">',"</span>"],
 "underline" : ['<span class="underline"','</span>'],
 "italic" : ['<span class="italic"','</span>']
 }
 (end)
 
 Esempio di file delle traduzioni in italiano
 (start code)
 var HoofEd_buttons_it = {
 "bold" : "Grassetto",
 "underline" : "Sottolineato",
 "italic" : "Corsivo"
 }
 (end)
 
 */
var HoofEd = new Class({
    Implements: [Events, Options],
    options: {
        buttonsFileName: 'HoofEd_buttons',
        basePath: './hoofed',
        textareaID: "textarea", //id della textarea
        toolElement: "p",
        toolClass: "HoofEd_tools",
        toolPosition: "before", //posizione dei tools: before | after
        buttons: [], //oggetto contenente Codici!
        lang: 'it',
        separatorClass: "hoofed_separator",
        separatorElement: "span",
        separatorText: " | "
    
    },
    
    /*
     Function: initialize
     *Di servizio* chiamato al momento dell'istanziazione.
     
     Parameters:
     options - obj che racchiude i parametri di istanza: <textareaID> <toolClass> <toolPosition> <button>
     */
    initialize: function(options){ //brodo primordiale: set delle opzioni di istanza...
        this.setOptions(options);
        this.options.Map = new Hash();
        this.options.ShortCutMap = new Hash();
        //this.options.Map.combine(this.options.buttons);
        this.createButtonMap();
        this.injectTools();
        this.addShortCuts();
        
    },
    
    addShortCuts: function(){
    
        var map = this.options.ShortCutMap;
        
        $(this.options.textareaID).addEvent("keydown", function(event, map){
            if ((event.control && this.has(event.key)) || (event.key == 'enter' && this.has('enter'))) {
                event.preventDefault();
                event.stopPropagation();
                this.get(event.key).fireEvent('click');
                return true;
            }
        }
.bindWithEvent(map));
        
        return true;
    },
    
    /*
     Function: injectTools
     *Di servizio* inietta la toolbar dove richiesto
     
     */
    injectTools: function(){
        //istanzio un nuovo elemendo DOM di tipo P e lo inietto prima della rootId
        this.toolBar = new Element(this.options.toolElement, {
            'class': this.options.toolClass,
            'styles': {
                width: $(this.options.textareaID).getSize().x
            }
        }).inject($(this.options.textareaID), this.options.toolPosition);
        
        //per ogni elemento della mappa, creo il bottone e lo inietto
        
        var mapLength = this.options.Map.getLength();
        
        var current = 0;
        $each(this.options.Map, function(value, key, index){
            current = current + 1;
            var shortcut = null;
            if ($defined(value[2])) {
                shortcut = value[2];
            }
            var createdButton = this.createButton(key, value[0], value[1], shortcut);
            createdButton.inject(this.toolBar, 'bottom');
            
            //hack per lo spazio tra i due link
            //this.toolBar.appendText(' | ','bottom');
            //span.appendText(" | ");
            
            if (current < mapLength) {
                var span = new Element(this.options.separatorElement, {
                    "class": this.options.separatorClass
                });
                span.appendText(this.options.separatorText);
                span.inject(this.toolBar, "bottom");
            }
            
        }, this);
        
    },
    
    /*
     Function: createButtonMap
     *Di servizio* crea la mappa dei bottoni (etichette + tags)
     
     Carica dinamicamente il file con la configurazione dei bottoni/tag disponibili e carica dinamicamente il file delle etichette nella lingua selezionata
     
     Vengono richiamati i file con suffisso *<buttonsFileName>*.
     
     Il file contentente il set di bottoni disponibili si chiama *<buttonsFileName>.js* mentre il file della lingua *<buttonsFileName>_<lang>.js*.
     
     Al caricamento di questi due file vengono richiamati i tag e le etichette, necessarie a comporre l'editor che si vuole istanziare, e caricate nella mappa principale dell'editor
     
     */
    createButtonMap: function(){
        
		var hoofed_obj = {
	        "HoofEd_buttons": null,
	        "labels": null
		};
		var myJSONRemote = new Request.JSON({
			"async": false,
			"url": this.options.basePath + '/' + this.options.buttonsFileName + '.js',
			"link": "cancel", 
			onSuccess: function(response){
				this.HoofEd_buttons = response;
			}.bind(hoofed_obj)
		});
		myJSONRemote.get(null);
		
		myJSONRemote = new Request.JSON({
			"async": false,
			"url": this.options.basePath + '/' + this.options.buttonsFileName + '_' + this.options.lang + '.js',
			"link": "cancel", 
			onSuccess: function(response){
				this.labels = response;
			}.bind(hoofed_obj)
		});
		myJSONRemote.get(null);
		
		for (var i = 0; i< this.options.buttons.length;i++) {
			var button = this.options.buttons[i];
			var label = hoofed_obj.labels[button];
			if (!$defined(label)) {
				label = button;
			}
			this.options.Map.include(label, hoofed_obj.HoofEd_buttons[button]);
		}
    },
    
    /*
     Funtion: createButton
     Crea un nuovo bottone nella toolbar
     
     Parameters:
     lab - etichetta del nuovo bottone
     startCode - tag di apertura
     endCode - tag di chiusura
     
     */
    createButton: function(lab, startCode, endCode, shortcut){
        //nuovo bottone
        var b = new Element('a');
        
        //setto href a niente
        b.set('href', '#');
        
        //setto l'etichetta del link
        b.set('text', lab);
        
        //just for testing
        // b.set('id','tool'+$random(1,9000));
        //setto il tag
        
        // inutile!
        //b.set('BBcode',lab);
        
        //aggiungo l'evento click di cui blocco la propagazione
        this.addEventButton(b);
        
        //aggiungo la mappa degli eventi
        if ($defined(shortcut) && shortcut != null) {
            this.options.ShortCutMap.include(shortcut, b);
        }
        
        //rendo a cesare quel che è di cesare
        return b;
        
    },
    
    /*
     Function: addEventButton
     *Di servizio* aggiunge gli eventi necessari ai bottoni della toolbar
     
     Parameters:
     el - elemento del dom che rappresenta il bottone
     */
    addEventButton: function(el){
        el.addEvents({
            'click': function(el){
                //ad ogni click: prendo il tagcode corretto  
                this.encloseSelection(el.get('text'));
                return false;
            }
.pass(el, this)
        });
    },
    
    /*
     Function: encloseSelection
     Servizio che viene chiamato per applicare i tag
     
     - legge dalla mappa dei tag, gli startTag e gli endTag
     - legge la selezione nella textarea
     - applica il tab
     */
    encloseSelection: function(key){
        //prendo il tag dalla mappa...
        var tagCode = this.options.Map.get(key);
        
        //setto il marcatore di apertura (delimitatori + valore in posizione 0)
        var prefix = tagCode[0];
        
        //setto il marcatore di chiusura (delimitatori + valore in posizione 1)
        var suffix = tagCode[1];
        
        //prendo il riferimento la textarea...
        var textarea = $(this.options.textareaID);
        
        //codice cross browser... da qui in poi non ci sono personalizzazioni.
        textarea.focus();
        
        var start, end, sel, scrollPos, subst;
        if (typeof(document["selection"]) != "undefined") {
            sel = document.selection.createRange().text;
        }
        else 
            if (typeof(textarea["setSelectionRange"]) != "undefined") {
                start = textarea.selectionStart;
                end = textarea.selectionEnd;
                scrollPos = textarea.scrollTop;
                sel = textarea.value.substring(start, end);
            }
        
        if (sel.match(/ $/)) { // exclude ending space char, if any
            sel = sel.substring(0, sel.length - 1);
            suffix = suffix + " ";
        }
        
        subst = prefix + sel + suffix;
        if (typeof(document["selection"]) != "undefined") {
            var range = document.selection.createRange().text = subst;
            textarea.caretPos -= suffix.length;
        }
        else 
            if (typeof(textarea["setSelectionRange"]) != "undefined") {
                textarea.value = textarea.value.substring(0, start) + subst +
                textarea.value.substring(end);
                if (sel) {
                    textarea.setSelectionRange(start + subst.length, start + subst.length);
                }
                else {
                    textarea.setSelectionRange(start + prefix.length, start + prefix.length);
                }
                textarea.scrollTop = scrollPos;
            }
    }
});
