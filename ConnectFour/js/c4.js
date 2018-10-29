
/**Colin Spratt
 * Connect-Four Project
 * Javascript Source
 */

//Player Constructor | Params: Name, Day, Month, and Year of Birth
function Player(name, day, month, year){
    this.name = name;
    this.birthday = new Date(year, month, day);
    this.turn = 0;
    this.chipsLeft = 21;
    this.streak = 0;
}

//GameSave Constructor
function GameSave(Winner, Loser, gameTime){
    this.winner = Winner;
    this.loser = Loser;
    this.gametime = gameTime;
}

//Parallel Board Data Constructor | Params: Row Count, Column Count
function Board(rows, cols){
    var cells = new Array(rows);
    for(var i = 0; i < rows; i++){
        cells[i] = new Array(cols);
        for(var j = 0; j < cols; j++){
            cells[i][j] = 0;
        }
    }
    //Cells: 0 for empty, 1 for black, 2 for red
    this.cells = cells;
    this.rows = rows;
    this.cols = cols;
}

//Function: Is a older than b? | Params: Millisecond A, Millisecond B
function by_age(a, b){
    return b.birthday.valueOf() - a.birthday.valueOf();
}

//Drop the Chips by One
function updateChips(thePlayer){
    thePlayer.chipsLeft--;
    if(curPlayer == 1){
        document.getElementById("p1chipcount").innerHTML = thePlayer.chipsLeft;
    }
    else{
        document.getElementById("p2chipcount").innerHTML = thePlayer.chipsLeft;
    }
}

var gTime = 0;
//Refresh the Timer
function updateTimer(){
    gTime += 1000;
    var elapsed = new Date(gTime);
    document.getElementById("gametime").innerHTML = elapsed.getMinutes() + ":" + elapsed.getSeconds(); 
}


//Finds the Longest Streak for the Player
function longestStreak(thePlayer, theBoard){
    var rows = theBoard.rows, cols = theBoard.cols;
    var match = thePlayer.turn;
    var curStreak = 0;
    for(var i = rows-1; i >= 0; i--){
        for(var j = 0; j < cols; j++){
            if(theBoard.cells[i][j] == match){
                var newStreak = checkNW(match, theBoard.cells, i, j);
                var next = checkN(match , theBoard.cells, i, j);
                if(next > newStreak)
                    newStreak = next;

                next = checkNE(match , theBoard.cells, i, j);
                if(next > newStreak)
                    newStreak = next;

                next = checkE(match, theBoard.cells, i, j);
                if(next > newStreak)
                    newStreak = next;
                
                if(newStreak > curStreak)
                    curStreak = newStreak;
            }
        }
    }
    return curStreak;
}

//-------------longestStreak Helper Functions-------------

function checkNW(match, cells, row, col){
    //console.log("Checking (" + row + "," + col +") for NW neighbors");
    var curStreak = 1;
    var newRow = row - 1;
    var newCol = col - 1;
    
    if(newRow < 0 || newRow > 5 || newCol < 0 || newCol > 6){
        return curStreak;
    }

    while((cells[newRow][newCol] != null) && (cells[newRow][newCol] == match)){
        curStreak++;
        newRow--;
        newCol--;
        if(newRow < 0 || newRow > 5 || newCol < 0 || newCol > 6){
            break;
        }
    }
    
    return curStreak;
}

function checkN(match, cells, row, col){
    //console.log("Checking (" + row + "," + col +") for N neighbors");
    var curStreak = 1;
    var newRow = row - 1;
    var newCol = col;
    
    if(newRow < 0 || newRow > 5 || newCol < 0 || newCol > 6){
        return curStreak;
    
    }
    while((cells[newRow][newCol] != null) && (cells[newRow][newCol] == match)){
        curStreak++;
        newRow--;
        if(newRow < 0 || newRow > 5 || newCol < 0 || newCol > 6){
            break;
        }
    }
    
    return curStreak;
}

function checkNE(match, cells, row, col){
    //console.log("Checking (" + row + "," + col +") for NE neighbors");
    var curStreak = 1;
    var newRow = row - 1;
    var newCol = col + 1;
    
    if(newRow < 0 || newRow > 5 || newCol < 0 || newCol > 6){
        return curStreak;
    }

    while((cells[newRow][newCol] != null) && (cells[newRow][newCol] == match)){
        curStreak++;
        newRow--;
        newCol++;
        if(newRow < 0 || newRow > 5 || newCol < 0 || newCol > 6){
            break;
        }
    }
    
    return curStreak;
    }

function checkE(match, cells, row, col){
    //console.log("Checking (" + row + "," + col +") for E neighbors");
    var curStreak = 1;
    var newRow = row;
    var newCol = col + 1;
   
    if(newRow < 0 || newRow > 5 || newCol < 0 || newCol > 6){
        return curStreak;
    }

    while((cells[newRow][newCol] != null) && (cells[newRow][newCol] == match)){
        curStreak++;
        newCol++;
        if(newRow < 0 || newRow > 5 || newCol < 0 || newCol > 6){
            break;
        }
    }
    return curStreak;
}

//--------------------------------------------------------

//Function: Places new Chip on Board | Params: Event, and Game Board
function addChip(event, board, FirstPlayer, SecondPlayer){
    var th = event.target;
    var details = th.getAttribute("id");
    var thePlayer;
    if(FirstPlayer.turn == curPlayer){
         thePlayer = FirstPlayer;
    }
    else{thePlayer = SecondPlayer;}

    //Pull Column number from ID
    var col = details.substring(6);

    //Find which cell to insert
    var foundCell = false;
    for(var i = (board.rows-1); i >= 0; i--){
        if(board.cells[i][col] == 0){
            //console.log("Found Empty Cell at (" + i + "," + col + ")");
            //Set New Value, Retrieve Cell
            foundCell = true;
            board.cells[i][col] = curPlayer;
            var cellImg = document.getElementById("irow" + i + "col" + col);
           
            //Set Black or Red
            if(curPlayer == 1){
                cellImg.setAttribute("src", "images/red-circle.png");
                updateChips(thePlayer);
            }
            else{
                cellImg.setAttribute("src", "images/black-circle.png");
                updateChips(thePlayer);
            }
            break;
        }
    } 
    if(foundCell == false){
        alert("Column is Full, select another.");
    }
    else{ 
        //console.log("Finding " + thePlayer.name + "'s longest streak.");
        thePlayer.streak = longestStreak(thePlayer, board);
        if(curPlayer == 1){curPlayer = 2;}
        else{curPlayer = 1;}
        if(thePlayer.streak < 4){
            alert(thePlayer.name + " needs " + (4-thePlayer.streak) + " to Connect Four!");
            nextTurn(FirstPlayer, SecondPlayer);
        }
        else{
            var otherPlayer;
            if(thePlayer.turn == 1){
                otherPlayer = SecondPlayer;
            }
            else{
                otherPlayer = FirstPlayer;
            }
            winGame(thePlayer, otherPlayer);
        }
    }
    
}

//Function: Create Chip Counts
function buildP1Chips(pname){
    var chipDiv = document.createElement("div");
    var name = document.createElement("h2");
    var chipsLeft = document.createElement("h3");
    name.id = "p1name";
    chipsLeft.id = "p1chipcount";
    name.innerHTML = pname + "'s Chips";
    chipsLeft.innerHTML = 21;
    chipDiv.appendChild(name);
    chipDiv.appendChild(chipsLeft);
    return chipDiv;
}

function buildP2Chips(pname){
    var chipDiv = document.createElement("div");
    var name = document.createElement("h2");
    var chipsLeft = document.createElement("h3");
    name.id = "p2name";
    chipsLeft.id = "p2chipcount";
    name.innerHTML = pname + "'s Chips";
    chipsLeft.innerHTML = 21;
    chipDiv.appendChild(name);
    chipDiv.appendChild(chipsLeft);
    return chipDiv;
}

//Function: Create Timer
function buildTimer(){
    var timer = document.createElement("h1");
    timer.id = "gametime";
    timer.innerHTML = "00:00";
    return timer;
}
//Function: Create the Board, Populate Rows/Columns
function buildBoard(board){
    var table = document.createElement('table');
    var tbody = document.createElement('tbody');
    var thead = document.createElement('thead');
    var rowOne = document.createElement('tr');
    var thnot = document.createElement('tr');
    thnot.innerHTML = " ";
    rowOne.appendChild(thnot);
    //Populate Column Headers, ID'd Columni
    for(var i = 0; i < board.cols; i++){
        var columnHead = document.createElement('th');
        columnHead.setAttribute("id", "Column" + i);
        switch(i){
            case 0:
                columnHead.innerHTML = "A";
                break;
            case 1:
                columnHead.innerHTML = "B";
                break;
            case 2:
                columnHead.innerHTML = "C";
                break;
            case 3:
                columnHead.innerHTML = "D";
                break;
            case 4:
                columnHead.innerHTML = "E";
                break;
            case 5:
                columnHead.innerHTML = "F";
                break;      
            case 6:
                columnHead.innerHTML = "G";
                break;   
        }
        rowOne.appendChild(columnHead);
    }
    thead.appendChild(rowOne);
    table.appendChild(thead);

    for(var i = 0; i < board.rows; i++){

        //Each Row is of class rowi
        var row = document.createElement('tr');
        row.classList.add('row' + i);
        var newHead = document.createElement("th");
        newHead.innerHTML = (i+1);
        row.appendChild(newHead);
        

        for(var j = 0; j < board.cols; j++){

            //Each Column is of class colj, and of id rowicolj
            var col = document.createElement('td');
            col.classList.add('col' + j);
            col.setAttribute("id", ("row" + i + "col" + j))
            var chip = document.createElement('img');
            chip.setAttribute("id", "i" + col.id);

            //Prefill Cell with blank chip, set corresponding to 0
            chip.setAttribute("src", "images/white-circle.png");
            board.cells[i][j] = 0;
            col.appendChild(chip);
            row.appendChild(col);
        }
        tbody.appendChild(row);
    }
    table.appendChild(tbody);
    return table;
}

//Declare two Players
var PlayerOne, PlayerTwo, curPlayer, startTime;
localStorage.clear();

//Request Player Name
var player1Name = prompt("Player 1: Enter your name", "John Smith");

//Regular Expression for Collecting Date of Birth
var date = /(\w+)[\W]+(\w+)[\W]*(\w+)/;

var dateIsValid = false;

//Is the DOB valid? If so pull out the capture groups.
while(dateIsValid == false){

    //Collect Date of Birth
    var player1DOB = prompt("Player 1: Enter your Date of Birth", "MM/DD/YYYY");
    var bday = date.exec(player1DOB);
    if(player1DOB.match(date) != null){
        var match = player1DOB.match(date);
        var month = match[1], day = match[2], year = match[3];

        //See if Month is Valid
        var match2 = month.match("/[0-9]+/g");
        if(match2 == null){
            month = month.toUpperCase();
            
            if(month === "JAN" || month === "JANUARY"){
                month = 1;
            }
            else if(month === "FEB" || month === "FEBRUARY"){
                month = 2
            }
            else if(month === "MAR" || month === "MARCH"){
                month = 3;
            }
            else if(month === "APR" || month === "APRIL"){
                month = 4;
            }
            else if(month === "MAY"){
                month = 5;
            }
            else if(month === "JUN" || month === "JUNE"){
                month = 6;
            }
            else if(month === "JUL" || month === "JULY"){
                month = 7;
            }
            else if(month === "AUG" || month === "AUGUST"){
                month = 8;
            }
            else if(month === "SEP" || month === "SEPTEMBER"){
                month = 9;
            }
            else if(month === "OCT" || month === "OCTOBER"){
                month = 10;
            }
            else if(month === "NOV" || month === "NOVEMBER"){
                month = 11;
            }
            else if(month === "DEC" || month === "DECEMBER"){
                month = 12;
            }

            //Month is 0 indexed
            month -= 1;
        }

        //Check if it's valid
        if((day > 0 && day <= 31) && (month >= 0 && month < 12) && (year > 0 && year < 2019)){
            
            //If year is 0-99 it defaults to 20th century
            if(year < 19){
                year += 2000;
            }
            PlayerOne = new Player(player1Name, day, month, year);
            dateIsValid = true;
        }
    }
}

//Reset DateIsValid, check Player 2
dateIsValid = false;
var player2Name = prompt("Player 2: Enter your name", "John Smith");

while(dateIsValid == false){

    //Collect Date of Birth
    var player2DOB = prompt("Player 2: Enter your Date of Birth", "MM/DD/YYYY");
    
    if(date.test(player2DOB) == true){
        var match = player2DOB.match(date);
        var month = match[1], day = match[2], year = match[3];

        //See if Month is Valid
        var match2 = month.match("/[0-9]+/g");
        if(match2 == null){
            month = month.toUpperCase();
            
            if(month === "JAN" || month === "JANUARY"){
                month = 1;
            }
            else if(month === "FEB" || month === "FEBRUARY"){
                month = 2
            }
            else if(month === "MAR" || month === "MARCH"){
                month = 3;
            }
            else if(month === "APR" || month === "APRIL"){
                month = 4;
            }
            else if(month === "MAY"){
                month = 5;
            }
            else if(month === "JUN" || month === "JUNE"){
                month = 6;
            }
            else if(month === "JUL" || month === "JULY"){
                month = 7;
            }
            else if(month === "AUG" || month === "AUGUST"){
                month = 8;
            }
            else if(month === "SEP" || month === "SEPTEMBER"){
                month = 9;
            }
            else if(month === "OCT" || month === "OCTOBER"){
                month = 10;
            }
            else if(month === "NOV" || month === "NOVEMBER"){
                month = 11;
            }
            else if(month === "DEC" || month === "DECEMBER"){
                month = 12;
            }

            //Month is 0 indexed
            month -= 1;
        }

        //Check if it's valid
        if((day > 0 && day <= 31) && (month >= 0 && month < 12) && (year > 0 && year < 2019)){
            
            //If year is 0-99 it defaults to 20th century
            if(year < 19){
                year += 2000;
            }
            PlayerTwo = new Player(player2Name, day, month, year);
            dateIsValid = true;
        }
    }
}

//Check who is Oldest
if(by_age(PlayerOne, PlayerTwo) > 0){
    alert(PlayerOne.name + " is older, so they go first.", "Start");
    PlayerOne.turn = 1;
    PlayerTwo.turn = 2;
    Start(PlayerOne, PlayerTwo);
}else{
    alert(PlayerTwo.name + " is older, so they go first.", "Start");
    PlayerOne.turn = 2;
    PlayerTwo.turn = 1;
    Start(PlayerTwo, PlayerOne);
}

var inter;

//Start the Game
function Start(FirstPlayer, SecondPlayer){
    //Initialize Players
    FirstPlayer.streak = 0;
    FirstPlayer.chipsLeft = 21;
    SecondPlayer.streak = 0;
    SecondPlayer.chipsLeft = 21;
    
    //Set Current Player, Create Board
    curPlayer = 1;
    var board = new Board(6, 7);

    //Populate Page, remove previous elements
    if(document.getElementById("board").hasChildNodes())
        document.getElementById("board").removeChild(document.getElementById("board").childNodes[0]);
    var newB = buildBoard(board);
    document.getElementById("board").appendChild(newB);

    if(document.getElementById("timer").hasChildNodes())
        document.getElementById("timer").removeChild(document.getElementById("timer").childNodes[0]);
    var timer = buildTimer();
    document.getElementById("timer").appendChild(timer);

    if(document.getElementById("p1chips").hasChildNodes())
        document.getElementById("p1chips").removeChild(document.getElementById("p1chips").childNodes[0]);
    var fpDiv = buildP1Chips(FirstPlayer.name);
    document.getElementById("p1chips").appendChild(fpDiv);

    if(document.getElementById("p2chips").hasChildNodes())
        document.getElementById("p2chips").removeChild(document.getElementById("p2chips").childNodes[0]);
    var spDiv = buildP2Chips(SecondPlayer.name);
    document.getElementById("p2chips").appendChild(spDiv);
    
    if(curPlayer == 1){
        alert("Click OK to begin " + FirstPlayer.name + "'s turn.", "OK");
    }
    else{
        alert("Click OK to begin " + SecondPlayer.name + "'s turn.", "OK");
    }

    //Set Start Time, Start Timer, Populate Events
    startTime = Date.now();
    gTime = 0;
    
    clearInterval(inter);
    inter = setInterval(updateTimer, 1000);
    
    for(var i = 0; i < board.cols; i++){
        var colClick = document.getElementById("Column" + i);
        colClick.addEventListener("click", function(e){addChip(e, board, FirstPlayer, SecondPlayer)});
    }
}

//Prompt for next turn
function nextTurn(FirstPlayer, SecondPlayer){
    if(curPlayer == 1){
        alert("Click OK to begin " + FirstPlayer.name + "'s turn.", "OK");
    }
    else{
        alert("Click OK to begin " + SecondPlayer.name + "'s turn.", "OK");
    }
}

//Won the game, write to storage and display top times
function winGame(Winner, Loser){
    if(document.getElementById("board").hasChildNodes())
        document.getElementById("board").removeChild(document.getElementById("board").childNodes[0]);
    var gameDuration = new Date(Date.now() - startTime);
    alert("Congratulations! " + Winner.name + " won in " + gameDuration.getMinutes() + ":" + gameDuration.getSeconds() + "!");
    var save = new GameSave(Winner, Loser, gameDuration.getTime());
    var allSaves = getSaves();
    if(allSaves === null){
        allSaves = new Array();
        allSaves[0] = save;
    }
    else{
        //console.log("allSaves already populated.");
        for(var i = 0; i <= allSaves.length; i++){
            if(allSaves[i] == null){
                //console.log("Position " + i + " is null.");
                allSaves[i] = save;
                break;
            }else if(dateCompare(save, allSaves[i]) == 1){
                //console.log("Added new save and sorting.");
                allSaves[allSaves.length-1] = save;
                allSaves.sort(dateCompare);
                break;
            }
        }
    }
    populateSaves(allSaves);
    printSaves(allSaves);

    Start(Winner, Loser);
}

//Save Printer
function printSaves(allSaves){
    var print = "Top Shortest Games\n";
    var i = 0;
    allSaves.forEach(function(save){
        if(save !== null){
            var duration = new Date(save.gametime);
            print += save.winner.name + " beat " + save.loser.name + " in " + duration.getMinutes() + ":" + duration.getSeconds() + "\n";
            i++;
        }
    })
    for(var j = i; j <= 10; j++){
        print+= "<EMPTY>\n";
    }
    alert(print, "New Game");
}

//Save helper function
function getSaves(){
    var wins = new Array();
    if(localStorage.length === 0){
        return null;
    }
    for(var i = 0; i < localStorage.length; i++){
        //console.logconsole.log("Pulling save " + i + " from storage.");
        var jsonObj = localStorage.getItem(i);
        //console.log("JSONObject: " + jsonObj);
        var obj = JSON.parse(jsonObj);
        //console.log(obj);
        wins[i] = obj;
    }
    localStorage.clear();
    return wins;
}

//a shorter than b: return 1 | b shorter than a: return -1 | b=a: return 0
function dateCompare(a,b){
    //console.log("a is GameSave: " + (a instanceof GameSave));
    //console.log("b is GameSave: " + (b instanceof GameSave));
    var aNum = a.gametime;
    var bNum = b.gametime;

    if(aNum < bNum){
        return 1;
    }
    else if(aNum > bNum){
        return -1;
    }
    return 0;
}

function populateSaves(allSaves){
    for(var i = 0; i < allSaves.length; i++){
        //console.log("Saving index " + i + " to localStorage.");
        localStorage.setItem(i, JSON.stringify(allSaves[i]));
    }
}