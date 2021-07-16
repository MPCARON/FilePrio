import java.util.ArrayList;

/**
 * Classe qui implémente l'interface IFilePrio.
 * L'implantation se fait à l'aide d'une liste chainée de maillons.
 *
 * @author Marie-Pier Caron
 * Code Permanent: CARM01609207
 * Courriel: caron.marie-pier.3@courrier.uqam.ca
 * Cours: INF1120-20
 * @version 1.0
 * @param <T> le type des elements dans cette file de priorité.
 *           Le type des elements doit implementer l'interface ITachePrio.
 */

public class FilePrioChainee<T extends ITachePrio> implements IFilePrio<T> {

    //Maillons de la liste
    private Maillon<T> elements;

    //Taille qui correspond au nombre d'éléments dans la liste.
    private int taille = 0;

    //Constantes
    public static final int PRIORITE_MIN = 1;

    /**
     * Constructeur qui crée initialement une file de priorité vide
     */
    public FilePrioChainee() {
        elements = new Maillon<T>(null);
    }

    /**
     * Enfile l'element (non null dans la file de priorite.
     *
     * @param element l'element a enfiler dans cette file de priorite.
     * @throws NullPointerException si l'element donne en parametre est null.
     */
    public void enfiler(T element) {
        Maillon<T> nouveauMaillon = new Maillon<T>(element);
        Maillon<T> tmp;

        if (element == null) {
            throw new NullPointerException();
        }

        if (estVide()) {
            elements = nouveauMaillon;
        } else if (elements.getInfo().getPriorite() < element.getPriorite()) {
            nouveauMaillon.setSuivant(elements);
            elements = nouveauMaillon;
        } else {
            tmp = elements;
            while (tmp.getSuivant() != null
                   && tmp.getSuivant().getInfo().getPriorite()
                   >= element.getPriorite()) {
                tmp = tmp.getSuivant();
            }
            if (tmp.getSuivant() != null) {
                nouveauMaillon.setSuivant(tmp.getSuivant());
            }
            tmp.setSuivant(nouveauMaillon);
        }
        taille++;
    }

    /**
     * Defile l'element le plus prioritaire (premier arrivee de la plus grande
     * priorite) de cette file de priorite.
     *
     * @return l'element defile.
     * @throws FileVideException si cete file de priorite est vide avant
     *                           l'appel de la méthode.
     */
    public T defiler() throws FileVideException {
        T info = null;

        if (estVide()) {
            throw new FileVideException();
        } else {
            info = elements.getInfo();
            elements = elements.getSuivant();
            taille--;
        }
        return info;
    }

    /**
     * Defile l'element le plus prioritaire de la priorite donnee en parametre.
     * Si aucun element de la priorite donnee n'existe dans cette file de
     * priorite, la methode retourne null et cette file de priorite n'est pas
     * modifiee.
     *
     * @param priorite la priorite de l'element a defiler.
     * @return l'element defile ou null si aucun element de la priorite donnee
     * en parametre n'existe dans cette file de priorite.
     * @throws FileVideException si cette file de priorite est vide avant qu'on
     *                           ne tente de defiler l'element.
     */
    public T defiler(int priorite) throws FileVideException {
        T info = null;
        Maillon<T> tmp = elements;

        if (estVide()) {
            throw new FileVideException();
        }

        if (!prioriteExiste(priorite)) {
            return info;
        } else if (tmp.getInfo().getPriorite() == priorite) {
            info = elements.getInfo();
            elements = elements.getSuivant();
        } else {
            tmp = rechercheElements(priorite);
            info = tmp.getSuivant().getInfo();
            if (tmp.getSuivant().getSuivant() == null) {
                tmp.setSuivant(null);
            } else {
                tmp.setSuivant(tmp.getSuivant().getSuivant());
            }
        }
        taille--;
        return info;
    }

    /**
     * Defile tous les elements de la priorite donnee. Si aucun element de
     * cette priorite n'existe dans cette file de priorite, celle-ci n'est
     * pas modifiee. La methode retourne une file de priorite contenant tous
     * les elements defiles, dans le meme ordre que lorsqu'ils se trouvaient
     * dans cette file de priorite. Si aucun element n'est defile, la file
     * retournee est vide.
     *
     * @param priorite la priorite des elements a defiler de cette file de
     *                 priorite.
     * @return Une file de priorité contenant tous les elements defiles, dans
     * le même ordre.
     * @throws FileVideException si cette file de priorite est vide avant
     *                           l'appel de cette methode.
     */
    public FilePrioChainee<T> defilerTous(int priorite) throws FileVideException {
        FilePrioChainee<T> file = new FilePrioChainee<>();
        Maillon<T> tmp = elements;
        Maillon<T> suivant;

        if (estVide()) {
            throw new FileVideException();
        } else {
            file = sousFilePrio(priorite);
            if (!file.estVide()) {
                if (tmp.getInfo().getPriorite() != priorite) {
                    tmp = rechercheElements(priorite);
                }
                suivant = tmp.getSuivant();
                for (int i = 1; i < file.taille; i++) {
                    suivant = suivant.getSuivant();
                }

                if (elements.getInfo().getPriorite() == priorite) {
                    elements = suivant;
                } else {
                    tmp.setSuivant(suivant.getSuivant());
                }
                taille = taille - file.taille;
            }
            return file;
        }
    }

    /**
     * Verifie si cette file de priorite contient des elements ou non.
     *
     * @param priorite la priorite dont on veut verifier l'existence dans cette
     *                 file de priorite.
     * @return true si au moins un element ayant la priorite donne en parametre
     *         existe dans cette file de priorite.
     */
    public boolean prioriteExiste(int priorite) {
        boolean existe = false;
        Maillon<T> tmp = elements;

        while (tmp !=null && !existe) {
            if (tmp.getInfo().getPriorite() == priorite) {
                existe = true;
            }
            tmp = tmp.getSuivant();
        }
        return existe;
    }

    /**
     * Verifie si cette file de priorite contient des elements ou non.
     *
     * @return true si cette file de priorite ne contient aucun element, false
     *         sinon.
     */
    public boolean estVide() {
        return taille == 0;
    }

    /**
     * Permet d'obtenir le nombre d'elements contenus dans cette file de priorite.
     *
     * @return le nombre d'elements dans cette file de priorite.
     */
    public int taille() {
        return taille;
    }

    /**
     * Permet d'obtenir le nombre d'elements ayant la priorite donnee en
     * parametre qui sont contenus dans cette file de priorite.
     *
     * @param priorite la priorite des elements dont on veut le nombre.
     * @return le nombre d'elements ayant la priorite donnee en parametre
     * qui sont contenus dans cette file de priorite.
     */
    public int taille(int priorite) {
        FilePrioChainee<T> file = sousFilePrio(priorite);

        return file.taille;
    }

    /**
     * Permet de consulter l'element en tete de cette file de priorite, sans
     * modifier celle-ci. L'element en tete de file est toujours l'element
     * le plus ancien parmis ceux ayant la priorite la plus forte.
     *
     * @return l'element en tete de cette file de priorite.
     * @throws FileVideException si cette file de priorite est vide avant
     *         l'appel de cette methode.
     */
    public T premier() throws FileVideException {
        if (estVide()) {
            throw new FileVideException();
        }
        return elements.getInfo();
    }

    /**
     * Permet de consulter l'element le plus prioritaire de la priorite donnee
     * en parametre, sans modifier cette file de priorite. Si aucun element 
     * de la priorite donnee existe dans cette file de priorite, la methode 
     * retourne null.
     * 
     * @param priorite la priorite de l'element le plus prioritaire que l'on
     *                 veut consulter.
     * @return l'element le plus prioritaire de la priorite donnee en
     *         parametre.
     * @throws FileVideException si cette file de priorite est vide avant
     *         l'appel de cette methode.
     */
    public T premier(int priorite) throws FileVideException {
        FilePrioChainee<T> file;

        if (estVide()) {
            throw new FileVideException();
        }

        file = sousFilePrio(priorite);

        return file.elements.getInfo();
     }

    /**
     * Retire tous les elements de cette file de priorite. Apres l'appel de
     * cette methode, l'appel de la methode estVide() retourne true.
     */
    public void vider() {
        elements = null;
        taille = 0;
    }

    /**
     * Retourne une file de priorite contenant tous les elements ayant la
     * priorite donnee en parametre se trouvant dans cette file de priorite.
     * Les elements dans la file de priorite a retourner conserve l'ordre
     * dans lequel ils apparaissent dans cette file de priorite. Apres l'appel
     * de cette methode, cette file de priorite ne doit pas avoir ete modifiee.
     * De plus, si aucun element ayant la priorite donnee ne se trouve dans
     * cette file de priorite, la methode retourne une file de priorite vide.
     *
     * @param priorite la priorite des elements de la file de priorite a
     *        retourner.
     * @return une file de priorite contenant tous les elements ayant la
     *         priorite donnee en parametre se trouvant dans cette file
     *         de priorite.
     */
    public FilePrioChainee<T> sousFilePrio(int priorite) {
        FilePrioChainee<T> file = new FilePrioChainee<>();
        Maillon<T> tmp = elements;

        if (prioriteExiste(priorite)) {
            while (tmp.getInfo().getPriorite() != priorite) {
                tmp = tmp.getSuivant();
            }

            while (tmp != null && tmp.getInfo().getPriorite() == priorite) {
                file.enfiler(tmp.getInfo());
                    tmp = tmp.getSuivant();
            }
        }
        return file;
    }


    /**
     * Teste si cette file de priorite contient au moins un element identique a
     * celui donne en parametre. Un element e1 est identique a un element e2
     * si e1.equals(e2) retourne true.
     *
     * @param elem l'element dont on teste l'existence.
     * @return true s'il existe au moins un element dans cette file de priorite
     *         qui est identique a celui donne en parametre, false sinon.
     */
    public boolean contient(T elem) {
        boolean exist = false;
        Maillon<T> tmp = elements;

        while (tmp != null && !exist) {
            if (tmp.getInfo().equals(elem)) {
                exist = true;
            }
            tmp = tmp.getSuivant();
        }
        return exist;
    }

    /**
     * Normalise les priorites des elements de cette file de priorite
     * en modifiant celles-ci pour que la plus petite priorite devienne 1, que
     * la deuxieme plus petite priorite devienne 2, et ainsi de suite, jusqu'a
     * la plus grande priorite (qui correspondra au nombre de priorites
     * differentes dans cette file de priorite).
     *
     */
    public void normaliser() {
        ArrayList<Integer> priorites = new ArrayList<>();
        Integer prio;
        Maillon <T> tmp = elements;
        int i;
        T info = null;

        while (tmp != null) {
            prio = tmp.getInfo().getPriorite();;
            if (!priorites.contains(prio)) {
                priorites.add(prio);
            }
            tmp = tmp.getSuivant();
        }

        i = priorites.size();
        tmp = elements;

        while (tmp != null) {
            info = tmp.getInfo();
            prio = info.getPriorite();
            info.setPriorite(i);
            if (tmp.getSuivant() != null
                && tmp.getSuivant().getInfo().getPriorite() !=  prio) {
                i--;
            }
            tmp = tmp.getSuivant();
        }
    }

    /**
     * Elimine les doublons de cette file de priorite. Si une sousFile de
     * priorite contient, par exemple, 3 elements identiques, la methode
     * elimine les deux moins prioritaire (en terme du moment d'entree
     * dans la file).
     *
     */
    public void eliminerDoublons() {
        Maillon<T> comparable = elements;
        Maillon<T> suivant;
        Maillon<T> prec;

        while (comparable != null && comparable.getSuivant() != null) {
            suivant = comparable.getSuivant();
            prec = comparable;
            while (suivant != null) {
                if (suivant.getInfo().equals(comparable.getInfo())) {
                    prec.setSuivant(suivant.getSuivant());
                } else {
                    prec = prec.getSuivant();
                }
                suivant = suivant.getSuivant();
            }
            comparable = comparable.getSuivant();
        }
    }

    /**
     * Permet d'obtenir la priorite la plus grande parmi les priorites de tous
     * les elements de cette file de priorite.
     *
     * @return la priorite maximum dans cette file de priorite.
     * @throws FileVideException si cette file de priorite est vide avant
     *         l'appel de cette methode.
     */
    public int prioriteMax() throws FileVideException {
        int prioriteMax;

        if (estVide()) {
            throw new FileVideException();
        } else {
            prioriteMax =  elements.getInfo().getPriorite();
        }

        return prioriteMax;
    }

    /**
     * Permet d'obtenir la priorite la plus petite parmi les priorites de tous
     * les elements de cette file de priorite.
     *
     * @return la priorite minimum dans cette file de priorite.
     * @throws FileVideException si cette file de priorite est vide avant
     *         l'appel de cette methode.
     */
    public int prioriteMin() throws FileVideException{
        int prioriteMin;
        Maillon <T> tmp = elements;

        if (estVide()) {
            throw new FileVideException();
        } else {
            while (tmp.getSuivant() != null) {
                tmp = tmp.getSuivant();
            }
            prioriteMin = tmp.getInfo().getPriorite();
        }
        return prioriteMin;
    }

    /**
     * Retourne une copie de cette file de priorite.
     *
     * @return une copie de cette file de priorite.
     */
    public FilePrioChainee<T> copie (){
        FilePrioChainee<T> copieFile = new FilePrioChainee<>();
        Maillon<T> tmp = elements;

        while (tmp != null) {
            copieFile.enfiler(tmp.getInfo());
            tmp = tmp.getSuivant();
        }

        return copieFile;
    }

    /**
     * Construit une representation sous forme de chaine de caracteres de cette
     * file de priorite.
     * @return une representation sous forme de chaine de caracteres de cette
     *         file de priorite.
     */
    @Override
    public String toString() {
        String s = "tete [ ";
        Maillon<T> tmp = elements;
        if (tmp == null) {
            s = s + " ] fin";
        } else {
            while (tmp != null) {
                s = s + tmp.getInfo() + ", ";
                tmp = tmp.getSuivant();
            }
            s = s.substring(0, s.length() -2) + " ] fin";
        }
        return s;
    }

    /**
     * Recherche un maillon dans la file de priorite
     *
     * @param priorite la priorite de l'element recherché
     * @return le maillon de la file de priorite qui est recherché
     */
    private Maillon<T> rechercheElements(int priorite) {
        Maillon<T> m = elements;

        while (m.getSuivant() != null
               && m.getSuivant().getInfo().getPriorite() != priorite) {
            m = m.getSuivant();
        }
        return m;
    }
}

