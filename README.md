# Number Generator
Generate a series of numbers in descending order, given the start number and the step

# OVERVIEW
This API writes into a file /tmp/{TASK_ID}_output.txt in the descending order  ,a sequence of numbers in the decreasing order till 0 , with step and start number given  as input  ;The First API returns a task;When the status of the task is called, the second API , it has to return appropriate status SUCCESS if done or IN_PROGRESS if task is still running;

The third API when called with a completed task should return the list of numbers reading from the file;

             API_1

            POST /api/generate

            {

               "Goal":"10",

               "Step":"2"

            }

            Return

            202 ACCEPTED

            {
                        "task":"UUID of the task",
            }

            API_2

            GET /api/tasks/{UUID of the task}/status

            return

            { "result":"SUCCESS/IN_PROGRESS/ERROR" }

            API_3

            GET /api/tasks/{UUID of the task}?action=get_numlist

            {
                        "result": "10,8,6,4,2,0"
            }

# HOW To RUN
Build the project using command: mvn package

Run the jar using command: java -jar target/numbergenerator-0.0.1-SNAPSHOT.jar

