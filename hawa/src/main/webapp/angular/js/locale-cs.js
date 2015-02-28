
app.config(function ($translateProvider) {	
	$translateProvider.translations('cs', {
		'Home': 'Hlavní stránka',
		'Outdoor': 'Počasí venku',
		'Security': 'Zabezpečení domu',
		'Live View': 'Skrytá kamera',
		'System State': 'Stav systému',

		'English': 'Anglicky',
		'Czech': 'Česky',
		
		'Controls': 'Ovládání',
		'Switch of all lights': 'Zhasnout vše',
		
		'Detail': 'Detail',
		'Name': 'Místnost',
		'Floor': 'Podlaží',
		'Current Temperature': 'Aktuální teplota',
		'Windows / doors': 'Okna / Dveře',
		
		'Snapshot': 'Uložit',
		'Some issue with the camera': 'Problém s kamerou',
		
		'Temperature °C': 'Teplota (°C)',
		'Pressure hPa': 'Atmosferický tlak (hPa)',
		'Humidity %': 'Relativní vlhkost (%)',
		
		'Security Mode': 'Mód',
		'Empty House': 'Prázdný dům',
		'Full House': 'Plný dům',
		'Security Off': 'Vypnuto',
		'Security Rules': 'Pravidla',
		'Enabled': 'Aktivní',
		'Disabled': 'Neaktivní',
		'Apply changes': 'Použít',
		
		// security rules
		'A window in basement opened (full house)': 'Otevřeno okno v přízemí (plný dům)',
		'Door or garage opened (empty or full house)': 'Hlavní nebo garážové dveře otevřeny (prázdný nebo plný dům)',
		'Temperature is too high': 'Vysoká teplota',
		'Motion detected by camera inside the house (empty house)': 'Kamera v domě detekovala pohyb (prázdný dům)',
		'Motion detected inside any hall (empty house)': 'Kamera v hale detekovala pohyb (prázdný dům)',
		'Motion detected outside the house - security still on 10 minutes later (empty or full house)': 'Detekce pohybu venku - zabezpečení zapnuté 10 minut poté (prázdný nebo plný dům)',
		'Window opened (empty house)': 'Otevřeno okno (prázdný dům)',
		
		'Event Detail': 'Detail události',
		'When': 'Kdy',
		'What': 'Co',
		'Where': 'Kde',
		'Dismiss': 'Zrušit',
		
		'alarm dismissed': 'alarm zrušen',
		
		// security types
		'Detected motion (sensor)': 'Detekován pohyb (senzor)',
		'Detected motion (camera)': 'Detekován pohyb (kamera)',
		'Door opened': 'Dveře otevřeny',
		'Garage opened': 'Garáž otevřena',
		'Window opened': 'Okno otevřeno',
		'High temperature': 'Vysoká teplota',
		
		'System Log': 'Log systému',
		'Application Log': 'Log aplikace',
		'System Actions': 'Akce',
		'Shutdown the system': 'Vypnout systém',
		
		'1 day': '1 den',
		'2 days': '2 dny',
		'3 days': '3 dny',
		'Week': 'týden',
		
		'Lights': 'Světla',
		'Motion Sensor': 'Pohybový senzor',
		'Light Switch': 'Vypínač',
		'Light Control': 'Světlo',
		'Light Level': 'Osvětlení',
		'Unknown': 'Neznámé',
		
		'Uptime': 'Spuštěno',
		'processors': 'procesorů',
		'heap': 'pamět',
		'Processor': 'Procesor',
		
		'Not present': 'Chybí',
		'On': '1',
		'Off': '0',
	
		'Home Automation': 'Správa domu',
		'Operating System': 'Operační systém',
		'Database is not running!': 'Problém s databází',
		
		// rooms
		'Hall down': 'Hala (přízemí)',
		'Kitchen': 'Kuchyně',
		'Living room': 'Obývací pokoj',
		'Bathroom down': 'Koupelna (přízemí)',
		'WC down': 'WC (přízemí)',
		'Down room': 'Pokoj pro hosty',
		'Hall up': 'Hala (1. patro)',
		'Bedroom': 'Ložnice',
		'Child room 1': 'Dětský pokoj 1',
		'Child room 2': 'Dětský pokoj 2',
		'Child room 3': 'Dětský pokoj 3',
		'Child room 4': 'Dětský pokoj 4',
		'Bathroom bed': 'Koupelna (ložnice)',
		'WC bed': 'WC (ložnice)',
		'Bathroom up': 'Koupelna (1. patro)',
		'WC up': 'WC (1. patro)',
		'Laundry room': 'Prádelna',
		
		'Basement': 'Přízemí',
		'First': 'První',
		
		// entries
		'Main door': 'Hlavní dveře',
		'Garage door': 'Garážové dveře',
		'West window': 'Západní okno',
		
		// sensor readings
		'temperature' : 'teplota', 
		'pressure': 'atmosferický tlak', 
		'humidity': 'relativní vlhkost', 
		'light': 'osvětlení',
		
		// generic labels
		'Loading...': 'Načítám...',
		'Last Checked': 'Informace k: ',
		'Closed' : 'Zavřené',
		'Open': 'Otevřené',
		'Error': 'Chyba senzoru'
	});
});
