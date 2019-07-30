# CucumberBDD
Cucumber Framework
Cucumber Framework

====================How to add project from local to Github=============================

git init  ( Initialize the local directory as a Git repository)
git add . ( Add the files in your new local repository. This stages them for the first commit)
git commit -m "Message"  (Commit the files that you've staged in your local repository)
git remote add origin https://github.com/rakeshmishra361/CucumberBDD.git ( add the URL for the remote repository where your local repository will be pushed)
git remote -v ( add the URL for the remote repository where your local repository will be pushed.) 
git pull --rebase origin master ( Local commits on top of the newly updated origin/master)
git push origin master ( Push your changes in remote master)

git branch --set-upstream-to=origin/master master
git pull

==================Push changes from local branch to git hub =============================


git checkout master
git pull
git checkout -b "Local Branch"
git rebase master
Make your changes in local branch
git commit -am "Commit message"
git push
git push --set-upstream origin Rakesh_New ( When we are pushing new branch first time)
Now review changes and create pull request and merge the changes to master.

=================Someone made the changes in remote master==============================

git stash
git checkout master
git pull
git checkout "Local Branch"
git pull
git rebase master
git stash pop
git commit -am "Commit message"
git push
Now review changes and create pull request and merge the changes to master.


=====================General commands====================================================

git config --global user.name ""
git config --global user.email ""
clear  (Clear screen)
git log ( to See all commits)
git diff( see the differences)
git diff --cached ( it will tell you the difference in file in staging area)
git branch -D "Rakesh_Cert" 	: 	Delete branch from local
git checkout -- <file>  	:	Roll back changes whatever done in file in staging area
git clean -f -n			:       list untracked file for staging area
git clean -f			: 	Remove untraCKED FILE FROM STAGING AREA.
git reset --hard origin/master  :  	your local changes are bad then just remove them or reset your local master to the state on remote
git reset HEAD~1		:  	Rollback last commit on branch and retain changes
git checkout -b new old( Copy branch )

git branch -m <oldname> <newname>  ( Rename)
git branch -m <newname> ( Rename)
git checkout -b "new_branch" "old_branch" ( Create branch from existing branch)
git status -s ( it will give you short message for action like modified or add)
touch .gitignore ( Create git ignore file)

===============Temporarily ignore changes=============================================
During development it's convenient to stop tracking file changes to a file committed into your git repo. 
This is very convenient when customizing settings or configuration files that are part of your project 
source for your own work environment.

git update-index --assume-unchanged <file>
git update-index --no-assume-unchanged <file>

===============Permanently ignore changes to a file====================
If a file is already tracked by Git, adding that file to your .gitignore is not enough to ignore changes to the file. 
You also need to remove the information about the file from Git's index:

These steps will not delete the file from your system. They just tell Git to ignore future updates to the file.

Add the file in your .gitignore.
git rm --cached <file>
Commit the removal of the file and the updated .gitignore to your repo.Cucumber Framework

=============Rakesh Updates==============
Added new line
+++++++++++++++++
