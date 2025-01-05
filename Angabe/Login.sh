#!/bin/sh

# --------------------------------------------------
# Monster Trading Cards Game
# --------------------------------------------------
echo "CURL Testing for Monster Trading Cards Game"
echo "Syntax: MonsterTradingCards.sh [pause]"
echo "- pause: optional, if set, then script will pause after each block"
echo .


pauseFlag=0
for arg in "$@"; do
    if [ "$arg" == "pause" ]; then
        pauseFlag=1
        break
    fi
done

if [ $pauseFlag -eq 1 ]; then read -p "Press enter to continue..."; fi

# --------------------------------------------------
echo "1) Login Users"
curl -i -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"Username\":\"kienboec\", \"Password\":\"daniel\"}"
echo "should return HTTP 200 with generated token for the user, here: kienboec-mtcgToken"
echo .
curl -i -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"Username\":\"altenhof\", \"Password\":\"markus\"}"
echo "should return HTTP 200 with generated token for the user, here: altenhof-mtcgToken"
echo .
curl -i -X POST http://localhost:10001/sessions --header "Content-Type: application/json" -d "{\"Username\":\"admin\",    \"Password\":\"istrator\"}"
echo "should return HTTP 200 with generated token for the user, here: admin-mtcgToken"
echo .

if [ $pauseFlag -eq 1 ]; then read -p "Press enter to continue..."; fi