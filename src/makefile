JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
%.class: %.java
	$(JC) $(JFLAGS) $^

CLASSES = \
	Attribute.java \
	item.java \
	Classifier.java \
	subSetclass.java \
	ID3Node.java \
	FileHandler.java \
	ID3.java \
	UIAssignment4.java \
	main.java \

all : $(CLASSES)
%.class : %.java
	$(JC) $(JFLAGS) $<
clean: $(CLASSES:.java=.class)
