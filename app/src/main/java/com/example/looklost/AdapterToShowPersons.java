package com.example.looklost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdapterToShowPersons extends RecyclerView.Adapter<AdapterToShowPersons.MyViewHolder> {

    private Context context;
    private List<PersonModel> personModelList;
    private OnItemClickListener onItemClickListener;

    //Multiple View for home fragment and item fragment
    private static final int TYPE_ALL = 0;
    private static final int TYPE_SEE_MORE = 1;

    public AdapterToShowPersons(Context context) {
        this.context = context;
        personModelList = new ArrayList<>();
    }
    public void addPerson(PersonModel PersonModel){
        personModelList.add(PersonModel);
        notifyDataSetChanged();
    }
    public void clearPersons(){
        personModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        old
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        return new MyViewHolder(view);

//        new
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        if (viewType == TYPE_ALL) {
//            View view = layoutInflater.inflate(R.layout.list_items, parent, false);
//            return new MyViewHolder(view);
//        } else {
//            View view = layoutInflater.inflate(R.layout.list_item_seemore, parent, false);
//            return new SeeMoreViewHolder(view);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PersonModel personModel = personModelList.get(position);
        if(personModel.getPersonImage()!=null){
            holder.title_image.setVisibility(View.VISIBLE);
            Glide.with(context).load(personModel.getPersonImage()).into(holder.title_image);
        }
        else{
            holder.title_image.setVisibility(View.GONE);
        }
        holder.itemName.setText(personModel.getPersonName());
        holder.itemCategory.setText(personModel.getPersonCategory());
        holder.Description.setText(personModel.getPersonDescription());

        String uid = personModel.getUserId();

        //To add Image of User with Item
        //Getting UserName who posted the Lost item
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel userModel = documentSnapshot.toObject(UserModel.class);
                        holder.UserName.setText(userModel.getUserName());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "error Occured in AdpterToShowPersons", Toast.LENGTH_SHORT).show();
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(personModel);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return personModelList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(PersonModel personModel);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void deletePerson(PersonModel itemModel) {
        // Perform the delete action based on the itemModel
        // For example, delete the item from Firestore using its ID

        FirebaseFirestore.getInstance()
                .collection("Persons")
                .document(itemModel.getPersonId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // Item deleted successfully, update your UI if needed
                        Toast.makeText(context, "Person Deleted Successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle delete failure
                        Toast.makeText(context, "Error Occured while Deleting Item!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView itemName,itemCategory,UserName, Description;
        private ImageView title_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.tvTitle);
            itemCategory = itemView.findViewById(R.id.tvCategory);
            UserName = itemView.findViewById(R.id.tvUser);
            Description = itemView.findViewById(R.id.tvDescription);
            title_image = itemView.findViewById(R.id.title_image);

        }
    }
    public void showOnlyFirstItem(int count) {
        if (personModelList.size() > 1) {
            count = count -1;
            PersonModel person = personModelList.get(count);
            personModelList.clear();
            personModelList.add(person);
        }
    }

////
//
//    public void searchFilter(ArrayList<ItemModel> searchList) {
//        itemModelList=new ArrayList<>();
//        itemModelList.addAll(searchList);
//        notifyDataSetChanged();
//    }
}


// #### 2 View Changes ########
//
//    //For 2 Views
//    @Override
//    public int getItemViewType(int position) {
//        if (position == 2) { // Display "See More" button after 3rd item
//            return TYPE_SEE_MORE;
//        } else {
//            return TYPE_ALL;
//        }
//    }


//    For Home Frag View
//    public class SeeMoreViewHolder extends RecyclerView.ViewHolder {
//        // Declare your views here
//
//        public SeeMoreViewHolder(View itemView) {
//            super(itemView);
//            // Initialize your views here
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Handle "See More" button click here
//                }
//            });
//        }}